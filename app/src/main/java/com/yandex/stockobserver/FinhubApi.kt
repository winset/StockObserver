package com.yandex.stockobserver

import com.yandex.stockobserver.genralInfo.ETFHoldings
import retrofit2.http.GET
import retrofit2.http.Query

interface FinhubApi {

    @GET("etf/holdings?symbol=VOO")
    suspend fun getHoldings(
        @Query("skip") page:Int
    ):ETFHoldings

}