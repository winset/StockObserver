package com.yandex.stockobserver.genralInfo

import com.google.gson.annotations.SerializedName

class CompanyNewsItem(
    val summary: String,
    val image: String,
    val datetime: Int,
    val related: String,
    val id: Int,
    val source: String,
    val category: String,
    val headline: String,
    val url: String
) {
}