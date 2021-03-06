package com.yandex.stockobserver.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.yandex.stockobserver.BuildConfig
import com.yandex.stockobserver.api.ApiInterceptor
import com.yandex.stockobserver.api.CacheInterceptor
import com.yandex.stockobserver.api.FinhubApi
import com.yandex.stockobserver.api.QuoteWebsocket
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    @FinHubAPI
    fun provideClient(cache: Cache): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        clientBuilder.cache(cache)
        clientBuilder.addNetworkInterceptor(CacheInterceptor())
        clientBuilder.addInterceptor(ApiInterceptor())
        clientBuilder.addInterceptor(loggingInterceptor)
        return clientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
     fun httpCache(context:Context): Cache {
        val CACHE_SIZE = 5 * 1024 * 1024L // 5 MB
        return Cache(context.cacheDir, CACHE_SIZE)
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, @FinHubAPI okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideFinhubApi(retrofit: Retrofit): FinhubApi {
        return retrofit.create(FinhubApi::class.java)
    }

    @Provides
    @Singleton
    fun provideQuoteWebsocket():QuoteWebsocket{
        return QuoteWebsocket()
    }

}