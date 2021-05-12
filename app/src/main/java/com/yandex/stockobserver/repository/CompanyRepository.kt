package com.yandex.stockobserver.repository

import com.yandex.stockobserver.api.QuoteWebsocket
import com.yandex.stockobserver.model.*

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
     fun getCurrentPrice(symbol: String): QuoteWebsocket
    suspend fun closeQuoteWebSocket()
    suspend fun initQuoteSocket(symbol: String)
}