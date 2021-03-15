package com.yandex.stockobserver.genralInfo.dto

import com.google.gson.annotations.SerializedName
import com.yandex.stockobserver.genralInfo.Quote

data class QuoteDto(
    @field:SerializedName("c")
    val currentPrice: Double? = null,

    @field:SerializedName("pc")
    val prevClosePrice: Double? = null,

    @field:SerializedName("t")// idk what is this
    val total: Int? = null,

    @field:SerializedName("h")
    val highPrice: Double? = null,// of the day

    @field:SerializedName("l")
    val lowPrice: Double? = null,// of the day

    @field:SerializedName("o")
    val openPrice: Double? = null// of the day
) {

    fun converter(): Quote {
        return Quote(
            currentPrice = currentPrice ?: -1.0,
            prevClosePrice = prevClosePrice ?: -1.0,
            total = total ?: -1,
            highPrice = highPrice ?: -1.0,
            lowPrice = lowPrice ?: -1.0,
            openPrice = openPrice ?: -1.0
        )
    }

}