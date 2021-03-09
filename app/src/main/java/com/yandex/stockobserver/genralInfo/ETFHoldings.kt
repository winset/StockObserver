package com.yandex.stockobserver.genralInfo

import com.google.gson.annotations.SerializedName

data class ETFHoldings(

	@field:SerializedName("symbol")
	val symbol: String? = null,

	@field:SerializedName("atDate")
	val atDate: String? = null,

	@field:SerializedName("holdings")
	val holdings: List<HoldingsItem?>? = null,

	@field:SerializedName("numberOfHoldings")
	val numberOfHoldings: Int? = null
)