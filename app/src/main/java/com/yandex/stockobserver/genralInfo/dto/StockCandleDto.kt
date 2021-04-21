package com.yandex.stockobserver.genralInfo.dto

import com.google.gson.annotations.SerializedName
import com.yandex.stockobserver.genralInfo.StockCandle

data class StockCandleDto(

	@field:SerializedName("c")
	val closePrice: List<Double>? = null,

	@field:SerializedName("s")
	val status: String? = null,

	@field:SerializedName("t")
	val timestamp: List<Int>? = null,

	@field:SerializedName("v")
	val volumeData: List<Int>? = null,

	@field:SerializedName("h")
	val highPrice: List<Double>? = null,

	@field:SerializedName("l")
	val lowPrice: List<Double>? = null,

	@field:SerializedName("o")
	val openPrice: List<Double>? = null
) {
    fun convert(): StockCandle {
        val closePriceC: List<Double> = if (!closePrice.isNullOrEmpty()) {
			closePrice } else { emptyList() }

        val timestampC: List<Int> = if (!timestamp.isNullOrEmpty()) {
			timestamp } else { emptyList() }

        val volumeDataC: List<Int> = if (!volumeData.isNullOrEmpty()) {
			volumeData } else { emptyList() }

        val highPriceC: List<Double> = if (!highPrice.isNullOrEmpty()) {
			highPrice } else { emptyList() }

        val lowPriceC: List<Double> = if (!lowPrice.isNullOrEmpty()) {
			lowPrice } else { emptyList() }

		val openPriceC: List<Double> = if (!openPrice.isNullOrEmpty()) {
			openPrice } else { emptyList() }

        return StockCandle(
			closePrice = closePriceC,
			status = status ?: "no_data",
			timestamp = timestampC,
			volumeData = volumeDataC,
			highPrice = highPriceC,
			lowPrice = lowPriceC,
			openPrice = openPriceC
		)
    }
}