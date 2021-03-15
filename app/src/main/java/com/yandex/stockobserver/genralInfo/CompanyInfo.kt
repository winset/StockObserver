package com.yandex.stockobserver.genralInfo


class CompanyInfo(
    val symbol: String,
    val cusip: String,
    val name: String,
    val logo: String,
    val price:Double,
    val margin:Double,
    var isFavorite:Boolean = false
)