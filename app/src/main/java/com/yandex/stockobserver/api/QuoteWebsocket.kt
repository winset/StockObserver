package com.yandex.stockobserver.api

import android.util.Log
import com.squareup.moshi.Moshi
import com.yandex.stockobserver.BuildConfig
import com.yandex.stockobserver.genralInfo.ETFHoldings
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONObject
import java.net.URI
import javax.net.ssl.SSLSocketFactory

class QuoteWebsocket {
    lateinit var webSocketClient: WebSocketClient


     fun initWebSocket(holdingsList: ETFHoldings) {
        val url = BuildConfig.WEB_SOKET_URL
        val coinbaseUri = URI(url)
        createWebSocketClient(coinbaseUri, holdingsList)
        val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        webSocketClient.setSocketFactory(socketFactory)
        webSocketClient.connect()
    }

    private fun createWebSocketClient(coinbaseUri: URI?, holdingsList: ETFHoldings) {
        webSocketClient = object : WebSocketClient(coinbaseUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d("WEB", "onOpen: ")
                subscribe(holdingsList)
            }

            override fun onMessage(message: String?) {
                Log.d("WEB", "onMessage: ")
                setUpQuotes(message)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                unsubscribe(holdingsList)
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
            Log.d("WEB", "setUpQuotes: " + message)
        }
    }

    private fun subscribe(holdingsList: ETFHoldings) {
        val jObjectType = JSONObject()
        jObjectType.put("type", "subscribe")

        holdingsList.holdings.forEach {
            jObjectType.put("symbol", it.symbol)
            webSocketClient.send(
                jObjectType.toString()
            )
        }
        Log.d("WEB", "subscribe: " + jObjectType.toString())

    }

    private fun unsubscribe(holdingsList: ETFHoldings) {
        val jObjectType = JSONObject()
        jObjectType.put("type", "unsubscribe")

        holdingsList.holdings.forEach {
            jObjectType.put("symbol", it.symbol)
        }
    }

}