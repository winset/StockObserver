package com.yandex.stockobserver.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yandex.stockobserver.genralInfo.entitys.CompanyInfoEntity

@Database(
    entities = arrayOf(
        CompanyInfoEntity::class
    ), version = 1, exportSchema = false
)
abstract class StockDatabase : RoomDatabase() {
    abstract fun getFavoriteStock(): FavoriteDao
}