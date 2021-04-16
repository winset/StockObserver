package com.yandex.stockobserver.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.yandex.stockobserver.db.StockDatabase
import com.yandex.stockobserver.db.Storage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideStorage(applicationContext: Context): Storage {
        val db = Room.databaseBuilder(
            applicationContext,
            StockDatabase::class.java, "database"
        ).build()

        return Storage(db)
    }
}