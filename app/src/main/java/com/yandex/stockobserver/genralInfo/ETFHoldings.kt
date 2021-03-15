package com.yandex.stockobserver.genralInfo


data class ETFHoldings(
    val symbol: String,
    val atDate: String,
    val holdings: List<HoldingsItem>,
    val numberOfHoldings: Int
)
