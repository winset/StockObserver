package com.yandex.stockobserver.repository

import com.yandex.stockobserver.genralInfo.CompanyInfo
import com.yandex.stockobserver.genralInfo.ETFHoldings
import com.yandex.stockobserver.genralInfo.Hint
import com.yandex.stockobserver.genralInfo.SearchSimilar
import kotlinx.coroutines.flow.Flow

interface HoldingRepository {
    fun getVOOCompanies(holdingsList: ETFHoldings, page: Int): Flow<List<CompanyInfo>>
    fun getFavorites(): Flow<List<CompanyInfo>>
    suspend fun addFavorite(companyInfo: CompanyInfo)
    suspend fun deleteFavorite(symbol: String)
    fun getPopularHint(holdingsList: ETFHoldings): List<Hint>
    suspend fun getLookingHint():List<Hint>
    suspend fun addNewHint(symbol: String)
    suspend fun isHintInDB(symbol: String): Boolean
    suspend fun getSimilar(symbol: String): SearchSimilar
    suspend fun getHolding(page: Int): ETFHoldings
}