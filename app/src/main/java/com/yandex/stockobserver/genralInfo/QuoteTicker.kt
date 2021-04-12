package com.yandex.stockobserver.genralInfo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuoteTicker(
    val currentPrice:Int?
)
