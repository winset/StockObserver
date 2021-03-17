package com.yandex.stockobserver.genralInfo.dto

import com.google.gson.annotations.SerializedName

data class SimilarResponseDto(

	@field:SerializedName("result")
	val result: List<SimilarSymbolDto?>? = null,

	@field:SerializedName("count")
	val count: Int? = null
)
