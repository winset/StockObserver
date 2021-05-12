package com.yandex.stockobserver.model

data class Quote(
    val currentPrice: Double,
    val prevClosePrice: Double,
    val total: Int,
    val highPrice: Double,
    val lowPrice: Double,
    val openPrice: Double
)
