package com.yandex.stockobserver.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.yandex.stockobserver.R

class SummaryView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defAttributeSet: Int = 0
) : LinearLayout(
    context, attrs, defAttributeSet
) {
    private val TAG: String = "SummaryView"

    init {
        inflate(context, R.layout.summary_view, this)
    }

    fun createView() {

    }

}