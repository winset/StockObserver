package com.yandex.stockobserver.api

import com.google.gson.GsonBuilder
import com.yandex.stockobserver.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkModule {
    val api = provideApiService()
    private fun provideApiService(): FinhubApi {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        clientBuilder.addInterceptor(ApiInterceptor())
        clientBuilder.addInterceptor(loggingInterceptor)

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
        // .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        return retrofit.create(FinhubApi::class.java)
    }
}