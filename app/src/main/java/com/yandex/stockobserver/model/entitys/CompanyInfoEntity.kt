package com.yandex.stockobserver.model.entitys

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yandex.stockobserver.db.TableNames
import com.yandex.stockobserver.model.CompanyInfo

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
    @ColumnInfo(name = "prevClosePrice")
    val prevClosePrice:Double,
    @ColumnInfo(name = "margin")
    val margin: String,
    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean,

    ) {
    constructor(companyInfo: CompanyInfo) : this(
        symbol = companyInfo.symbol,
        cusip = companyInfo.cusip,
        name = companyInfo.name,
        logo = companyInfo.logo,
        price = companyInfo.price,
        prevClosePrice = companyInfo.prevClosePrice,
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
            prevClosePrice = prevClosePrice,
            margin = margin,
            isFavorite = isFavorite
        )
    }

}