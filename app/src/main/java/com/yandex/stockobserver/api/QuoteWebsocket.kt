package com.yandex.stockobserver.api

import android.util.Log
import com.google.gson.Gson
import com.yandex.stockobserver.BuildConfig
import com.yandex.stockobserver.model.QuoteTicker
import com.yandex.stockobserver.model.dto.QuoteTickerDto
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
                Log.d("WEB", "onMessage: " + message)
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

    private  fun setUpQuotes(message: String?) {
        Log.d("WEB", "setUpQuotes: " + message)
        message?.let {
            val quoteTicker = Gson().fromJson(message, QuoteTickerDto::class.java)
            if (quoteTicker != null) {
                messageHandler?.handleMessage(quoteTicker.convert())
            }
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


    private var messageHandler: MessageHandler? = null


    interface MessageHandler {
        fun handleMessage(quoteTicker: QuoteTicker)
    }

    fun addMessageHandler(msgHandler: MessageHandler) {
        this.messageHandler = msgHandler
    }


}