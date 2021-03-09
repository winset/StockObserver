package com.yandex.stockobserver.genralInfo

import com.google.gson.annotations.SerializedName

data class HoldingsItem(

	@field:SerializedName("symbol")
	val symbol: String? = null,

	@field:SerializedName("cusip")
	val cusip: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("share")
	val share: Float? = null,

	@field:SerializedName("percent")
	val percent: Double? = null,

	@field:SerializedName("value")
	val value: Float? = null,

	@field:SerializedName("isin")
	val isin: String? = null
)