package com.yandex.stockobserver.repository

import com.yandex.stockobserver.api.FinhubApi
import com.yandex.stockobserver.api.QuoteWebsocket
import com.yandex.stockobserver.db.Storage
import com.yandex.stockobserver.model.*
import com.yandex.stockobserver.model.entitys.CompanyInfoEntity
import javax.inject.Inject

class CompanyRepositoryImpl @Inject constructor(
    private val storage: Storage,
    private val api: FinhubApi,
    private val quoteWebsocket: QuoteWebsocket,
) : CompanyRepository {

    override suspend fun getStockCandle(symbol: String): StockCandle {
        return api.getStockCandle(symbol, "1", "1615298999", "1615302599").convert()
    }

    override suspend fun addFavorite(companyInfo: CompanyInfo) {
        storage.insertFavorite(CompanyInfoEntity(companyInfo))
    }

    override suspend fun deleteFavorite(symbol: String) {
        storage.deleteFromFavorite(symbol)
    }

    override suspend fun getNews(
        symbol: String,
        dateFrom: String,
        dateTo: String
    ): List<CompanyNewsItem> {
        return api.getCompanyNews(symbol, dateFrom, dateTo).map { it.convert() }
    }

    override suspend fun getGeneralInfoBySymbol(symbol: String): CompanyGeneral {
        return api.getCompanyGeneralInfoBySymbol(symbol).convert()
    }

    override fun getCurrentPrice(symbol: String): QuoteWebsocket {
            return quoteWebsocket
    }

    override suspend fun initQuoteSocket(symbol: String) {
        quoteWebsocket.initWebSocket(symbol)
    }

    override suspend fun closeQuoteWebSocket() {
        quoteWebsocket.webSocketClient.close()
    }


}