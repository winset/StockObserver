package com.yandex.stockobserver.model

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