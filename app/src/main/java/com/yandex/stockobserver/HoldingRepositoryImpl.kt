package com.yandex.stockobserver

import com.yandex.stockobserver.api.FinhubApi
import com.yandex.stockobserver.api.NetworkModule
import com.yandex.stockobserver.db.Storage
import com.yandex.stockobserver.db.StorageModule
import com.yandex.stockobserver.genralInfo.*
import com.yandex.stockobserver.genralInfo.dto.SearchSimilarDto
import com.yandex.stockobserver.genralInfo.entitys.CompanyInfoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode


class HoldingRepositoryImpl(
    private val api: FinhubApi = NetworkModule.api,
    private val storage: Storage = StorageModule.storage
) : HoldingRepository {

    private val batchSize = 15
    // private val holdingsList = runBlocking { getHolding(0) }


    fun getVOOCompanies(holdingsList: ETFHoldings, page: Int): Flow<List<CompanyInfo>> {
        return flow {
            var result = emptyList<CompanyInfo>()
            val favorites = mutableListOf<CompanyInfo>()
            getFavorites().collect {
                if (!it.isNullOrEmpty()) {
                    favorites.addAll(it)
                }
            }
            // val holdings = getHolding(0)
            val firstElement = (page) * batchSize
            if (firstElement + batchSize <= holdingsList.holdings.size) {
                result =
                    getCompanyInfo(holdingsList, firstElement, firstElement + batchSize, favorites)
            } else {
                result = getCompanyInfo(
                    holdingsList,
                    firstElement,
                    holdingsList.holdings.lastIndex,
                    favorites
                )
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun getFavorites(): Flow<List<CompanyInfo>> {
        return flow {
            emit(storage.getAllFavorite().map { it.convert() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addFavorite(companyInfo: CompanyInfo) {
        storage.insertFavorite(CompanyInfoEntity(companyInfo))
    }

    suspend fun deleteFavorite(cusip: String) {
        storage.deleteFromFavorite(cusip)
    }

    fun getPopularHint(holdingsList: ETFHoldings): List<Hint> {
        val hintList = mutableListOf<Hint>()
        val popular = holdingsList.holdings.shuffled().subList(0, 10)
        popular.forEach {
            hintList.add(Hint(it.name))
        }
        return hintList
    }

    suspend fun getLookingHint() {

    }


    private suspend fun getCompanyInfo(
        holdingsList: ETFHoldings,
        firstIndex: Int,
        lastIndex: Int,
        favorites: List<CompanyInfo>
    ): List<CompanyInfo> {
        val result = mutableListOf<CompanyInfo>()
        val companyInfo = mutableListOf<CompanyGeneral>()
        val quotes = mutableListOf<Quote>()
        val subList = holdingsList.holdings.subList(firstIndex, lastIndex)
        subList.forEach {
            companyInfo.add(getGeneralInfo(it.cusip))
        }

        subList.forEach {
            quotes.add(getQuote(it.symbol))
        }

        subList.forEachIndexed { index, holdingsItem ->
            val context = MathContext(5, RoundingMode.HALF_UP)
            val openPrice = BigDecimal(quotes[index].prevClosePrice, context).toDouble()
            val currentPrice = BigDecimal(quotes[index].currentPrice, context).toDouble()
            val margin = currentPrice - openPrice
            val result1 = BigDecimal(margin, context)
            var isFavorite: Boolean = false
            favorites.forEach {
                if (it.cusip == holdingsItem.cusip) {
                    isFavorite = true
                }
            }

            result.add(
                CompanyInfo(
                    holdingsItem.symbol,
                    holdingsItem.cusip,
                    holdingsItem.name,
                    companyInfo[index].logo,
                    quotes[index].currentPrice,
                    result1.toDouble(),
                    isFavorite
                )
            )
        }
        return result
    }


    suspend fun getSimilar(symbol: String): SearchSimilar {
        return api.getSimilarSymbol(symbol).convert()
    }

    suspend fun getHolding(page: Int): ETFHoldings {
        return api.getHoldings(page).convert()
    }

    private suspend fun getGeneralInfo(cusip: String): CompanyGeneral {
        return api.getCompanyGeneralInfo(cusip).convert()
    }

    private suspend fun getQuote(symbol: String): Quote {
        return api.getQuote(symbol).converter()
    }

}