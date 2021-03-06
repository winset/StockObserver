package com.yandex.stockobserver.model

data class CompanyGeneral(
    val finnhubIndustry: String,
    val country: String,
    val ticker: String,
    val marketCapitalization: Double,
    val phone: String,
    val weburl: String,
    val name: String,
    val ipo: String,
    val logo: String,
    val currency: String,
    val exchange: String,
    val shareOutstanding: Double
)
