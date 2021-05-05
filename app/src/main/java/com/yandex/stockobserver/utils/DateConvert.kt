package com.yandex.stockobserver.utils

import java.text.SimpleDateFormat
import java.util.*

fun convertFromUnixToDateString(date: Int): String {
    val sdf = SimpleDateFormat("dd/MM/yy")
    val netDate = Date(date * 1000L)
    return sdf.format(netDate)
}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun Date.plusOneWeek(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    this.time -= 604800000
    return formatter.format(this)
}
