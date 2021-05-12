package com.yandex.stockobserver.model.dto

import com.google.gson.annotations.SerializedName
import com.yandex.stockobserver.model.QuoteTicker
import com.yandex.stockobserver.model.QuoteTickerItem

data class QuoteTickerDto(

    @field:SerializedName("data")
    val data: List<QuoteTickerItemDto>? = null,

    @field:SerializedName("type")
    val type: String? = null
) {
    fun convert(): QuoteTicker {
        val items = if (!data.isNullOrEmpty()) {
            data.map { it.convert() }
        } else {
            emptyList()
        }

        return QuoteTicker(
            data = items,
            type = type ?: ""
        )
    }
}

data class QuoteTickerItemDto(

    @field:SerializedName("p")
    val P: Double? = null,

    @field:SerializedName("c")
    val C: List<String>? = null,

    @field:SerializedName("s")
    val S: String? = null,

    @field:SerializedName("t")
    val T: Long? = null,

    @field:SerializedName("v")
    val V: Int? = null
) {
    fun convert(): QuoteTickerItem {
        val c: List<String> = if (!C.isNullOrEmpty()) {
            C
        } else {
            emptyList()
        }

        return QuoteTickerItem(
            currentPrice = P ?: -1.0,
            c,
            S = S ?: "",
            T = T ?: -1,
            V = V ?: -1
        )
    }
}
