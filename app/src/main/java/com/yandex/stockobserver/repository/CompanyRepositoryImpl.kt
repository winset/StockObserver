package com.yandex.stockobserver.repository

import com.yandex.stockobserver.api.FinhubApi
import com.yandex.stockobserver.api.QuoteWebsocket
import com.yandex.stockobserver.db.Storage
import com.yandex.stockobserver.genralInfo.StockCandle
import javax.inject.Inject

class CompanyRepositoryImpl@Inject constructor(
    private val storage: Storage,
    private val api: FinhubApi,
    private val quoteWebsocket: QuoteWebsocket,
):CompanyRepository {

    override suspend fun getStockCandle():StockCandle {
       return api.getStockCandle("AAPL","1","1615298999","1615302599").convert()
    }
}