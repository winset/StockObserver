package com.yandex.stockobserver.genralInfo


class StockCandle(
    val closePrice: List<Double>,
    val status: String,
    val timestamp: List<Int>,
    val volumeData: List<Int>,
    val highPrice: List<Double>,
    val lowPrice: List<Double>,
    val openPrice: List<Double>
)