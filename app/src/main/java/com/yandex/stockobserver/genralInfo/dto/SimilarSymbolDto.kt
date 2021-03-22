package com.yandex.stockobserver.genralInfo.dto

import com.google.gson.annotations.SerializedName
import com.yandex.stockobserver.genralInfo.SimilarSymbol

data class SimilarSymbolDto(

    @field:SerializedName("displaySymbol")
    val displaySymbol: String? = null,

    @field:SerializedName("symbol")
    val symbol: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("type")
    val type: String? = null
) {
    fun convert(): SimilarSymbol {
        return SimilarSymbol(
            displaySymbol = displaySymbol ?: "",
            symbol = symbol ?: "",
            description = description ?: "",
            type = type ?: ""
        )

    }
}