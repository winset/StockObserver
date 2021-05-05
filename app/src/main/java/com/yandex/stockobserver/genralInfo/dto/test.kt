package com.yandex.stockobserver.genralInfo.dto

import com.google.gson.annotations.SerializedName

data class Test(

	@field:SerializedName("test")
	val test: List<TestItem?>? = null
)

data class TestItem(

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
)
