package com.yandex.stockobserver.repository

import android.util.Log
import com.yandex.stockobserver.api.FinhubApi
import com.yandex.stockobserver.api.QuoteWebsocket
import com.yandex.stockobserver.db.Storage
import com.yandex.stockobserver.genralInfo.*
import com.yandex.stockobserver.genralInfo.entitys.CompanyInfoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import javax.inject.Inject
import javax.inject.Singleton


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
            emit(storage.getAllFavorite().map { it.convert() })
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

    suspend fun initQuoteSocket(holdingsList: ETFHoldings) {
        quoteWebsocket.initWebSocket(holdingsList)
    }

    suspend fun closeQuoteWebSocket() {
        quoteWebsocket.webSocketClient.close()
    }

    override suspend fun addNewHint(symbol: String) {
        storage.addHint(Hint(symbol))
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
        subList.forEach {
            companyInfo.add(getGeneralInfoBySymbol(it.symbol))
            quotes.add(getQuote(it.symbol))
        }

        subList.forEachIndexed { index, holdingsItem ->
            val context = MathContext(5, RoundingMode.HALF_UP)
            val openPrice = BigDecimal(quotes[index].prevClosePrice, context).toDouble()
            val currentPrice = BigDecimal(quotes[index].currentPrice, context).toDouble()
            val margin = currentPrice - openPrice
            val result1 = BigDecimal(margin, context)
            result.add(
                CompanyInfo(
                    holdingsItem.symbol,
                    holdingsItem.cusip,
                    companyInfo[index].name,
                    companyInfo[index].logo,
                    quotes[index].currentPrice,
                    result1.toDouble(),
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