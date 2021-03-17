package com.yandex.stockobserver.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yandex.stockobserver.R

class MainActivity : AppCompatActivity() {

    private val mainFragment = MainFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initMainFragment()
    }

    private fun initMainFragment() {
        supportFragmentManager.beginTransaction()
                .add(R.id.container, mainFragment).commit()
    }

}