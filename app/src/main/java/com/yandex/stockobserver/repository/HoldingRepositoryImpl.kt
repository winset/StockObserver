package com.yandex.stockobserver.repository

import com.yandex.stockobserver.api.FinhubApi
import com.yandex.stockobserver.api.QuoteWebsocket
import com.yandex.stockobserver.db.Storage
import com.yandex.stockobserver.model.*
import com.yandex.stockobserver.model.entitys.CompanyInfoEntity
import com.yandex.stockobserver.utils.marginToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class HoldingRepositoryImpl @Inject constructor(
    private val storage: Storage,
    private val api: FinhubApi,
    private val quoteWebsocket: QuoteWebsocket,
) : HoldingRepository {

    private val batchSize = 15

    override fun getVOOCompanies(holdingsList: ETFHoldings, page: Int): Flow<List<CompanyInfo>> {
        return flow {
            var result = emptyList<CompanyInfo>()
            val firstElement = (page) * batchSize

            if (firstElement + batchSize <= holdingsList.holdings.size) {
                result =
                    getCompanyInfo(holdingsList, firstElement, firstElement + batchSize)
            } else {
                result = getCompanyInfo(
                    holdingsList,
                    firstElement,
                    holdingsList.holdings.lastIndex
                )
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun getFavorites(): Flow<List<CompanyInfo>> {
        return flow {
            val favorite = storage.getAllFavorite().map { it.convert() }
            val result = mutableListOf<CompanyInfo>()
            favorite.forEach {
                val quote = getQuote(it.symbol)
                val margin = marginToString(quote.currentPrice, quote.prevClosePrice)
                val currentPrice = (Math.round(quote.currentPrice * 100).toDouble() / 100)
                result.add(
                    CompanyInfo(
                        it.symbol,
                        it.cusip,
                        it.name,
                        it.logo,
                        currentPrice,
                        quote.prevClosePrice,
                        margin,
                        it.isFavorite
                    )
                )
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun addFavorite(companyInfo: CompanyInfo) {
        storage.insertFavorite(CompanyInfoEntity(companyInfo))
    }

    override suspend fun deleteFavorite(symbol: String) {
        storage.deleteFromFavorite(symbol)
    }

    override fun getPopularHint(holdingsList: ETFHoldings): List<Hint> {
        val hintList = mutableListOf<Hint>()
        val popular = holdingsList.holdings.shuffled().subList(0, 10)
        popular.forEach {
            hintList.add(Hint(it.name))
        }
        return hintList
    }

    override suspend fun getLookingHint(): List<Hint> = storage.getAllHints()

    override suspend fun addNewHint(symbol: String) {
        storage.addHint(Hint(symbol))
    }

    override suspend fun isHintInDB(symbol: String): Boolean {
        return storage.isHintInDb(symbol)
    }

    private suspend fun getCompanyInfo(
        holdingsList: ETFHoldings,
        firstIndex: Int,
        lastIndex: Int
    ): List<CompanyInfo> {
        val result = mutableListOf<CompanyInfo>()
        val companyInfo = mutableListOf<CompanyGeneral>()
        val quotes = mutableListOf<Quote>()
        val subList = holdingsList.holdings.subList(firstIndex, lastIndex)

        subList.forEachIndexed { index, holdingsItem ->
            companyInfo.add(getGeneralInfoBySymbol(holdingsItem.symbol))
            quotes.add(getQuote(holdingsItem.symbol))
            val margin = marginToString(quotes[index].currentPrice, quotes[index].prevClosePrice)
            val currentPrice = (Math.round(quotes[index].currentPrice * 100).toDouble() / 100)
            result.add(
                CompanyInfo(
                    holdingsItem.symbol,
                    holdingsItem.cusip,
                    companyInfo[index].name,
                    companyInfo[index].logo,
                    currentPrice,
                    quotes[index].prevClosePrice,
                    margin,
                    isFavorite(holdingsItem.symbol)
                )
            )
        }
        return result
    }

    override suspend fun getSimilar(symbol: String): SearchSimilar {
        return api.getSimilarSymbol(symbol).convert()
    }

    override suspend fun getHolding(page: Int): ETFHoldings {
        return api.getHoldings(page).convert()
    }

    private suspend fun getGeneralInfo(cusip: String): CompanyGeneral {
        return api.getCompanyGeneralInfo(cusip).convert()
    }

    private suspend fun getGeneralInfoBySymbol(symbol: String): CompanyGeneral {
        return api.getCompanyGeneralInfoBySymbol(symbol).convert()
    }


    private suspend fun getQuote(symbol: String): Quote {
        return api.getQuote(symbol).converter()
    }

    private suspend fun isFavorite(symbol: String): Boolean {
        return storage.isFavorite(symbol)
    }
}