package com.yandex.stockobserver.repository

import com.yandex.stockobserver.genralInfo.*

interface CompanyRepository {
    suspend fun getStockCandle(symbol: String): StockCandle
    suspend fun addFavorite(companyInfo: CompanyInfo)
    suspend fun deleteFavorite(symbol: String)
    suspend fun getGeneralInfoBySymbol(symbol: String): CompanyGeneral
    suspend fun getNews(
        symbol: String,
        dateFrom: String,
        dateTo: String
    ): List<CompanyNewsItem>
    suspend fun getCurrentPrice(symbol: String):Double
}