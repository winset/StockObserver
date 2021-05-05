package com.yandex.stockobserver.genralInfo.dto

import com.google.gson.annotations.SerializedName
import com.yandex.stockobserver.genralInfo.CompanyNewsItem

data class CompanyNewsItemDto(

    @field:SerializedName("summary")
    val summary: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("datetime")
    val datetime: Int? = null,

    @field:SerializedName("related")
    val related: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("source")
    val source: String? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("headline")
    val headline: String? = null,

    @field:SerializedName("url")
    val url: String? = null
) {
    fun convert(): CompanyNewsItem {
        return CompanyNewsItem(
            summary = summary ?: "",
            image = image ?: "",
            datetime = datetime ?: 0,
            related = related ?: "",
            id = id ?: 0,
            source = source ?: "",
            category = category ?: "",
            headline = headline ?: "",
            url = url ?: ""
        )
    }
}