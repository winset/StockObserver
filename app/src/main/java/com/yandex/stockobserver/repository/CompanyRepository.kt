package com.yandex.stockobserver.repository

import com.yandex.stockobserver.genralInfo.*

interface CompanyRepository {
    suspend fun getStockCandle(symbol: String): StockCandle
    suspend fun addFavorite(companyInfo: CompanyInfo)
    suspend fun deleteFavorite(symbol: String)
    suspend fun getExecutive(symbol: String): CompanyExecutive
    suspend fun getNews(
        symbol: String,
        dateFrom: String,
        dateTo: String
    ): List<CompanyNewsItem>
}