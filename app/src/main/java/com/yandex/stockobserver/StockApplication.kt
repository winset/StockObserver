package com.yandex.stockobserver

import android.app.Application
import androidx.room.Room
import com.yandex.stockobserver.db.StockDatabase
import com.yandex.stockobserver.db.Storage
import com.yandex.stockobserver.di.AppComponent
import com.yandex.stockobserver.di.AppModule
import com.yandex.stockobserver.di.DaggerAppComponent

class StockApplication:Application() {
    companion object{
        lateinit var stockComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        stockComponent = initDagger()
    }

    private fun initDagger():AppComponent{
        return DaggerAppComponent.builder()
            .appModule(AppModule((this)))
            .build()
    }
}