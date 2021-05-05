package com.yandex.stockobserver.api

import com.yandex.stockobserver.genralInfo.StockCandle
import com.yandex.stockobserver.genralInfo.dto.*
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

const val CACHE_CONTROL_HEADER = "Cache-Control"
const val CACHE_CONTROL_NO_CACHE = "no-cache"
// @Headers("$CACHE_CONTROL_HEADER: $CACHE_CONTROL_NO_CACHE") this  can be used if we don't need to cache some data
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

    @GET("stock/executive")
    suspend fun getCompanyExecutive(
        @Query("symbol")symbol: String
    ):CompanyExecutiveDto

    @GET("company-news")
    suspend fun getCompanyNews(
        @Query("symbol")symbol: String,
        @Query("from")dateFrom:String,
        @Query("to")dateTo:String
    ):List<CompanyNewsItemDto>

}