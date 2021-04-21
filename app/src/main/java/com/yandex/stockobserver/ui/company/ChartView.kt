package com.yandex.stockobserver.ui.company

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.yandex.stockobserver.R

class ChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defAttributeSet: Int = 0
) : LinearLayout(
    context, attrs, defAttributeSet
) {
    private val TAG: String = "InfoView"

    init {
        inflate(context, R.layout.chart_view, this)
    }

    fun createView() {
        //https://finnhub.io/docs/api/earnings-calendar
    }

}