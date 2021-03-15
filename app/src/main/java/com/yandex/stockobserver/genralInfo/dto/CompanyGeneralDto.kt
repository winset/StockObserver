package com.yandex.stockobserver.genralInfo.dto

import com.google.gson.annotations.SerializedName
import com.yandex.stockobserver.genralInfo.CompanyGeneral

data class CompanyGeneralDto(

    @field:SerializedName("finnhubIndustry")
    val finnhubIndustry: String? = null,

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("ticker")
    val ticker: String? = null,

    @field:SerializedName("marketCapitalization")
    val marketCapitalization: Double? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("weburl")
    val weburl: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("ipo")
    val ipo: String? = null,

    @field:SerializedName("logo")
    val logo: String? = null,

    @field:SerializedName("currency")
    val currency: String? = null,

    @field:SerializedName("exchange")
    val exchange: String? = null,

    @field:SerializedName("shareOutstanding")
    val shareOutstanding: Double? = null
) {
    fun convert(): CompanyGeneral {
        return CompanyGeneral(
            finnhubIndustry = finnhubIndustry ?: "N/A",
            country = country ?: "N/A",
            ticker = ticker ?: "N/A",
            marketCapitalization = marketCapitalization ?: -1.0,
            phone = phone ?: "N/A",
            weburl = weburl ?: "N/A",
            name = name ?: "N/A",
            ipo = ipo ?: "N/A",
            logo = logo ?: "N/A",
            currency = currency ?: "N/A",
            exchange = exchange ?: "N/A",
            shareOutstanding = shareOutstanding ?: -1.0
        )
    }
}