package com.yandex.stockobserver.genralInfo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class CompanyInfo(
    val symbol: String,
    val cusip: String,
    val name: String,
    val logo: String,
    val price:Double,
    val prevClosePrice:Double,
    val margin:String,
    var isFavorite:Boolean = false
) : Parcelable