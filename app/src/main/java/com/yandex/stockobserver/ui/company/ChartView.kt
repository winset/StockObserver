package com.yandex.stockobserver.ui.company

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.yandex.stockobserver.databinding.ChartViewBinding
import com.yandex.stockobserver.genralInfo.StockCandle

class ChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defAttributeSet: Int = 0
) : ConstraintLayout(
    context, attrs, defAttributeSet
) {
    private val TAG: String = "ChartView"
    private var binding:ChartViewBinding =
        ChartViewBinding.inflate(LayoutInflater.from(context),this)

    init {
        // addView(binding.root)
    }

    fun createView(stockCandle: StockCandle) {
        Log.d(TAG, "createView: ")
        binding.chart.setData(stockCandle)
    }

    fun updateQuoteTicker(price:String,margin:String){
        binding.currentPrice.text = price
        binding.currentMargin.text = margin
    }

}