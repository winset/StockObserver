package com.yandex.stockobserver

import com.yandex.stockobserver.genralInfo.ETFHoldings

class HoldingRepositoryImpl(private val api: FinhubApi = NetworkModule.api) {

    suspend fun getHolding(page:Int):ETFHoldings{
        return api.getHoldings(page)
    }

}