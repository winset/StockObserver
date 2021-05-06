package com.yandex.stockobserver.ui.company

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.yandex.stockobserver.R
import com.yandex.stockobserver.databinding.SummaryViewBinding

class SummaryView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defAttributeSet: Int = 0
) : RelativeLayout(
    context, attrs, defAttributeSet
) {
    private val TAG: String = "SummaryView"
    private var binding:SummaryViewBinding = SummaryViewBinding.inflate(
        LayoutInflater.from(context),this)

    fun createView() {
        binding.textView.text = "123"
    }

}