package com.yandex.stockobserver.api

import android.util.Log
import com.yandex.stockobserver.BuildConfig
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import java.lang.Exception
import java.lang.IllegalStateException

class ApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url
        val token = if (TokenLife.isTokenAlive(TokenLife.FIRST_TOKEN)) {
            Log.d("TAG", "intercept11: ")
            BuildConfig.API_KEY
        } else if (TokenLife.isTokenAlive(TokenLife.SECOND_TOKEN)) {
            Log.d("TAG", "intercept22: ")
            BuildConfig.API_KEY1
        } else {
            return Response.Builder()
                .request(original)
                .protocol(Protocol.HTTP_1_1)
                .code(999)
                .message("\"Limit is reached, please wait.\"")
                .body(ResponseBody.create(null, "Limit is reached, please wait.")).build()
        }

        val requestBuilder = originalUrl.newBuilder()
            .addQueryParameter("token", token).build()


        val newUrl = original.newBuilder().url(requestBuilder).build()
        var response = chain.proceed(newUrl)

        if (isLimitReached(response.code)) {

            var currentToken = token

            if (currentToken == BuildConfig.API_KEY) {
                TokenLife.setTokenTime(TokenLife.FIRST_TOKEN)
                currentToken = BuildConfig.API_KEY1
            } else {
                TokenLife.setTokenTime(TokenLife.SECOND_TOKEN)
                currentToken = BuildConfig.API_KEY
            }

            val newRequest = originalUrl.newBuilder()
                .addQueryParameter("token", currentToken).build()
            val superNewUrl = original.newBuilder().url(newRequest).build()
            response = chain.proceed(superNewUrl)
        }

        if (isHaveAccess(response.code)){
            val currentToken = BuildConfig.SANDBOX_API_KEY
            val newRequest = originalUrl.newBuilder()
                .addQueryParameter("token", currentToken).build()
            val superNewUrl = original.newBuilder().url(newRequest).build()
            response = chain.proceed(superNewUrl)
        }

        if (isServerError(response.code)){
            return Response.Builder()
                .request(original)
                .protocol(Protocol.HTTP_1_1)
                .code(999)
                .message("\"Server problem((\"")
                .body(ResponseBody.create(null, "Server problem((")).build()
        }

        return response
    }

    private fun isLimitReached(code: Int): Boolean {
        return code == 429
    }

    private fun isHaveAccess(code: Int):Boolean{
        return code == 403
    }

    private fun isServerError(code: Int):Boolean{
        return code in 500..599
    }

}