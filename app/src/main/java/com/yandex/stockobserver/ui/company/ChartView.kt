package com.yandex.stockobserver.ui.company

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.yandex.stockobserver.R
import com.yandex.stockobserver.databinding.ChartViewBinding
import com.yandex.stockobserver.databinding.CompanyFragmentBinding
import com.yandex.stockobserver.genralInfo.StockCandle

class ChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defAttributeSet: Int = 0
) : ConstraintLayout(
    context, attrs, defAttributeSet
) {
    private val TAG: String = "ChartView"
    private var binding:ChartViewBinding

    init {
        binding = ChartViewBinding.inflate(LayoutInflater.from(context),this)
       // addView(binding.root)
    }

    fun createView(stockCandle: StockCandle) {
        //https://finnhub.io/docs/api/earnings-calendar
        Log.d(TAG, "createView: ")
        binding.chart.setData(stockCandle)
    }




}