package com.yandex.stockobserver.ui.company

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.yandex.stockobserver.R

class ForecastView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defAttributeSet: Int = 0
) : LinearLayout(
    context, attrs, defAttributeSet
) {
    private val TAG: String = "ForecastView"

    init {
        inflate(context, R.layout.forecast_view, this)
    }

    fun createView(){

    }

}