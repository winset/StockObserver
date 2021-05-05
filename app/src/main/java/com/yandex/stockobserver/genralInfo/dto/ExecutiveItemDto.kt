package com.yandex.stockobserver.genralInfo.dto

import com.google.gson.annotations.SerializedName
import com.yandex.stockobserver.genralInfo.ExecutiveItem

data class ExecutiveItemDto(

    @field:SerializedName("sex")
    val sex: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("compensation")
    val compensation: Int? = null,

    @field:SerializedName("currency")
    val currency: String? = null,

    @field:SerializedName("position")
    val position: String? = null,

    @field:SerializedName("age")
    val age: Int? = null,

    @field:SerializedName("since")
    val since: String? = null
) {
    fun convert(): ExecutiveItem {
        return ExecutiveItem(
            sex = sex ?: "",
            name = name ?: "",
            compensation = compensation ?: 0,
            currency = currency ?: "",
            position = position ?: "",
            age = age ?: 0,
            since = since ?: "",
        )
    }
}