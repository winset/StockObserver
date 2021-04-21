package com.yandex.stockobserver.ui.company

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.yandex.stockobserver.R

class NewsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defAttributeSet: Int = 0
) : LinearLayout(
    context, attrs, defAttributeSet
) {
    private val TAG: String = "NewsView"

    init {
        inflate(context, R.layout.news_view, this)
    }

    fun createView() {
        //https://mboum.com/api/v1/ne/news/?symbol=AAPL&apikey=demo
    }

}