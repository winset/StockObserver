package com.yandex.stockobserver.api

import android.util.Log

object TokenLife {
    const val FIRST_TOKEN = 0;
    const val SECOND_TOKEN = 1;

    private const val MINUTE = 60000
    var firstTokenTime: Long = -1
    var secondTokenTime: Long = -1


    fun isTokenAlive(tokenCode: Int): Boolean {
        return when (tokenCode) {
            FIRST_TOKEN -> if (firstTokenTime > 0) {
                firstTokenTime + MINUTE <= System.currentTimeMillis()
            } else true
            else -> if (secondTokenTime > 0){
                secondTokenTime + MINUTE <= System.currentTimeMillis()
            } else true
        }
    }
    fun setTokenTime(tokenCode: Int){
        when(tokenCode){
            FIRST_TOKEN-> firstTokenTime = System.currentTimeMillis()
            SECOND_TOKEN-> secondTokenTime = System.currentTimeMillis()
        }
    }

}