package com.yandex.stockobserver.api

import com.yandex.stockobserver.genralInfo.dto.CompanyGeneralDto
import com.yandex.stockobserver.genralInfo.dto.ETFHoldingsDto
import com.yandex.stockobserver.genralInfo.dto.QuoteDto
import com.yandex.stockobserver.genralInfo.dto.SimilarResponseDto
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

    @GET("quote")
    suspend fun getQuote(
        @Query("symbol") symbol:String
    ): QuoteDto

    @GET("search")
    suspend fun getSimilarSymbol(
        @Query("q") symbol: String
    ):SimilarResponseDto
}