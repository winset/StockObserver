package com.yandex.stockobserver.api

import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.yandex.stockobserver.BuildConfig
import com.yandex.stockobserver.genralInfo.ETFHoldings
import com.yandex.stockobserver.genralInfo.QuoteTicker
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONObject
import java.net.URI
import javax.net.ssl.SSLSocketFactory

class QuoteWebsocket {
    lateinit var webSocketClient: WebSocketClient


    fun initWebSocket(symbol: String) {
        val url = BuildConfig.WEB_SOKET_URL
        val coinbaseUri = URI(url)
        createWebSocketClient(coinbaseUri, symbol)
        val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        webSocketClient.setSocketFactory(socketFactory)
        webSocketClient.connect()
    }

    private fun createWebSocketClient(coinbaseUri: URI?, symbol: String) {
        webSocketClient = object : WebSocketClient(coinbaseUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d("WEB", "onOpen: ")
                subscribe(symbol)
            }

            override fun onMessage(message: String?) {
                Log.d("WEB", "onMessage: ")
                setUpQuotes(message)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                unsubscribe(symbol)
            }

            override fun onError(ex: java.lang.Exception?) {
                if (ex != null) {
                    Log.d("WEB", "onError: " + ex.message)
                }
            }
        }
    }

    private fun setUpQuotes(message: String?) {
        message?.let {
            val moshi = Moshi.Builder().build()
            val adapter: JsonAdapter<QuoteTicker> = moshi.adapter(QuoteTicker::class.java)
            val quoteTicker = adapter.fromJson(message)
            Log.d("WEB", "setUpQuotes: " + message)
        }
    }

    private fun subscribe(symbol: String) {
        val jObjectType = JSONObject()
        jObjectType.put("type", "subscribe")
        jObjectType.put("symbol", symbol)
        webSocketClient.send(
            jObjectType.toString()
        )
        Log.d("WEB", "subscribe: " + jObjectType.toString())

    }

    private fun unsubscribe(symbol: String) {
        val jObjectType = JSONObject()
        jObjectType.put("type", "unsubscribe")
        jObjectType.put("symbol", symbol)
    }

}