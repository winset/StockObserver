package com.yandex.stockobserver.model



class QuoteTickerItem(
    val currentPrice: Double,
    val C: List<String>,
    val S: String,
    val T: Long,
    val V: Int
)