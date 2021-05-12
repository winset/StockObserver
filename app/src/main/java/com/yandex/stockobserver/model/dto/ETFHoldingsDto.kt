package com.yandex.stockobserver.model.dto

import com.google.gson.annotations.SerializedName
import com.yandex.stockobserver.model.ETFHoldings

data class ETFHoldingsDto(

    @field:SerializedName("symbol")
    val symbol: String? = null,

    @field:SerializedName("atDate")
    val atDate: String? = null,

    @field:SerializedName("holdings")
    val holdings: List<HoldingsItemDto>? = null,

    @field:SerializedName("numberOfHoldings")
    val numberOfHoldings: Int? = null
) {
    fun convert(): ETFHoldings {
        val holdingsModel = holdings?.map { it.convert() }

        return ETFHoldings(
            symbol = symbol ?: "N/A",
            atDate = atDate ?: "N/A",
            holdings = holdingsModel ?: emptyList(),
            numberOfHoldings = numberOfHoldings ?: -1
        )
    }
}