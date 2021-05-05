package com.yandex.stockobserver.genralInfo.dto

import com.google.gson.annotations.SerializedName
import com.yandex.stockobserver.genralInfo.CompanyExecutive

data class CompanyExecutiveDto(

    @field:SerializedName("executive")
    val executiveDto: List<ExecutiveItemDto>? = null
) {
    fun convert(): CompanyExecutive {
        val executive = if (executiveDto.isNullOrEmpty())
            emptyList()
        else executiveDto.map { it.convert() }

        return CompanyExecutive(
            executive
        )
    }
}