package com.yandex.stockobserver.ui.company

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.yandex.stockobserver.genralInfo.StockCandle

class Chart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defAttributeSet: Int = 0,
    defStyleRes: Int = 0
): View(context, attrs,defAttributeSet,defStyleRes) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    fun setData(data:StockCandle){

    }

}
