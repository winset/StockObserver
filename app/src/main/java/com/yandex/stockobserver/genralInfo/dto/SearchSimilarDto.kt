package com.yandex.stockobserver.genralInfo.dto

import com.google.gson.annotations.SerializedName
import com.yandex.stockobserver.genralInfo.SearchSimilar

data class SearchSimilarDto(

    @field:SerializedName("result")
    val result: List<SimilarSymbolDto>? = null,

    @field:SerializedName("count")
    val count: Int? = null
) {
    fun convert(): SearchSimilar {
        val symbols = result?.map { it.convert() }

        return SearchSimilar(
            result = symbols ?: emptyList(),
            count = count ?: -1
        )
    }
}
