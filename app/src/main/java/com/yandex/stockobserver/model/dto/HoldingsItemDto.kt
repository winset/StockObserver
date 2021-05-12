package com.yandex.stockobserver.model.dto

import com.google.gson.annotations.SerializedName
import com.yandex.stockobserver.model.HoldingsItem

data class HoldingsItemDto(

    @field:SerializedName("symbol")
    val symbol: String? = null,

    @field:SerializedName("cusip")
    val cusip: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("share")
    val share: Float? = null,

    @field:SerializedName("percent")
    val percent: Double? = null,

    @field:SerializedName("value")
    val value: Float? = null,

    @field:SerializedName("isin")
    val isin: String? = null
) {
    fun convert(): HoldingsItem {
        return HoldingsItem(
            symbol = symbol ?: "N/A",
            cusip = cusip ?: "N/A",
            name = name ?: "N/A",
            share = share ?: -1.0f,
            percent = percent ?: -1.0,
            value = value ?: -1.0f,
            isin = isin ?: "N/A"
        )
    }
}