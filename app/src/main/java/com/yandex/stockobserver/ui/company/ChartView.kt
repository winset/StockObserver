package com.yandex.stockobserver.ui.company

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.yandex.stockobserver.R
import com.yandex.stockobserver.databinding.ChartViewBinding
import com.yandex.stockobserver.model.StockCandle
import com.yandex.stockobserver.utils.marginToString

class ChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defAttributeSet: Int = 0
) : ConstraintLayout(
    context, attrs, defAttributeSet
) {
    private val TAG: String = "ChartView"
    private var binding:ChartViewBinding =
        ChartViewBinding.inflate(LayoutInflater.from(context),this)

    fun createView(stockCandle: StockCandle) {
        Log.d(TAG, "createView: ")
        binding.chart.setData(stockCandle)
    }

    fun updateQuoteTicker(price:Double,prevClosePrice:Double){
        if ((Math.round((price - prevClosePrice)*100).toDouble()/100) > 0){
            binding.currentMargin.setTextColor(context.resources.getColor(R.color.green_text))
        }else{
            binding.currentMargin.setTextColor(context.resources.getColor(R.color.red_text))
        }
        binding.currentPrice.text = "$$price"
        binding.currentMargin.text = marginToString(price,prevClosePrice)
    }
}