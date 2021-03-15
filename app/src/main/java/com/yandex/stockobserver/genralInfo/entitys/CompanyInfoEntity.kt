package com.yandex.stockobserver.genralInfo.entitys

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yandex.stockobserver.db.TableNames
import com.yandex.stockobserver.genralInfo.CompanyInfo

@Entity(tableName = TableNames.GENERAL_INFO)
class CompanyInfoEntity(
    @ColumnInfo(name = "symbol")
    val symbol: String,
    @PrimaryKey
    @ColumnInfo(name = "cusip")
    val cusip: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "logo")
    val logo: String,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "margin")
    val margin: Double,
    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean,

    ) {
    constructor(companyInfo: CompanyInfo) : this(
        symbol = companyInfo.symbol,
        cusip = companyInfo.cusip,
        name = companyInfo.name,
        logo = companyInfo.logo,
        price = companyInfo.price,
        margin = companyInfo.margin,
        isFavorite = companyInfo.isFavorite
    )


    fun convert(): CompanyInfo {
        return CompanyInfo(
            symbol = symbol,
            cusip = cusip,
            name = name,
            logo = logo,
            price = price,
            margin = margin,
            isFavorite = isFavorite
        )
    }

}