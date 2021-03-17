package com.yandex.stockobserver

import android.app.Application
import androidx.room.Room
import com.yandex.stockobserver.db.StockDatabase
import com.yandex.stockobserver.db.Storage
import com.yandex.stockobserver.db.StorageModule

class StockApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        val db = Room.databaseBuilder(
            applicationContext,
            StockDatabase::class.java, "database"
        ).build()

        StorageModule.storage = Storage(db)

    }
}