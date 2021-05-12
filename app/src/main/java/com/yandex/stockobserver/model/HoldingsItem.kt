package com.yandex.stockobserver.model

data class HoldingsItem(
    val symbol: String,
    val cusip: String,
    val name: String,
    val share: Float,
    val percent: Double,
    val value: Float,
    val isin: String
)
