package com.yandex.stockobserver.api

import com.yandex.stockobserver.genralInfo.StockCandle
import com.yandex.stockobserver.genralInfo.dto.*
import retrofit2.http.GET
import retrofit2.http.Query

interface FinhubApi {

    @GET("etf/holdings?symbol=VOO")
    suspend fun getHoldings(
        @Query("skip") page:Int
    ): ETFHoldingsDto

    @GET("stock/profile2")
    suspend fun getCompanyGeneralInfo(
        @Query("cusip") cusip:String
    ): CompanyGeneralDto

    @GET("stock/profile2")
    suspend fun getCompanyGeneralInfoBySymbol(
        @Query("symbol") symbol:String
    ): CompanyGeneralDto

    @GET("quote")
    suspend fun getQuote(
        @Query("symbol") symbol:String
    ): QuoteDto

    @GET("search")
    suspend fun getSimilarSymbol(
        @Query("q") q: String
    ):SearchSimilarDto

    @GET("stock/candle")
    suspend fun getStockCandle(
        @Query("symbol")symbol: String,
        @Query("resolution") resolution:String,
        @Query("from") from:String,
        @Query("to") to:String
    ):StockCandleDto

}