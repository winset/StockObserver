package com.yandex.stockobserver.api

import com.yandex.stockobserver.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url
        val requestBuilder = originalUrl.newBuilder()
            .addQueryParameter("token", BuildConfig.API_KEY).build()

        val newUrl = original.newBuilder().url(requestBuilder).build()

        return chain.proceed(newUrl)
    }
}