package com.yandex.stockobserver.genralInfo

import com.google.gson.annotations.SerializedName

data class HoldingsItem(
    val symbol: String,
    val cusip: String,
    val name: String,
    val share: Float,
    val percent: Double,
    val value: Float,
    val isin: String
)
