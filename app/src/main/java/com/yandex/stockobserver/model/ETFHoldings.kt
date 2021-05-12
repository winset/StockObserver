package com.yandex.stockobserver.model


data class ETFHoldings(
    val symbol: String,
    val atDate: String,
    val holdings: List<HoldingsItem>,
    val numberOfHoldings: Int
)
