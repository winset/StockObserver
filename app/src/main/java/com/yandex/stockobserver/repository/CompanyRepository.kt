package com.yandex.stockobserver.repository

import com.yandex.stockobserver.genralInfo.StockCandle

interface CompanyRepository {
    suspend fun getStockCandle():StockCandle

}