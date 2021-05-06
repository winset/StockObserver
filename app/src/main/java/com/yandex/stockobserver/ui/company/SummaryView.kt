package com.yandex.stockobserver.ui.company

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.squareup.picasso.Picasso
import com.yandex.stockobserver.R
import com.yandex.stockobserver.databinding.SummaryViewBinding
import com.yandex.stockobserver.genralInfo.CompanyGeneral
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class SummaryView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defAttributeSet: Int = 0
) : RelativeLayout(
    context, attrs, defAttributeSet
) {
    private val TAG: String = "SummaryView"
    private var binding:SummaryViewBinding = SummaryViewBinding.inflate(
        LayoutInflater.from(context),this)

    fun createView(companyGeneral: CompanyGeneral) {
        binding.country.text = companyGeneral.country
        binding.currency.text = companyGeneral.currency
        binding.exchange.text = companyGeneral.exchange
        binding.ipo.text = companyGeneral.ipo
        binding.finnhubIndustry.text = companyGeneral.finnhubIndustry
        binding.marketCapitalization.text = companyGeneral.marketCapitalization.toString()
        binding.weburl.text = companyGeneral.weburl
        binding.shareOutstanding.text = companyGeneral.shareOutstanding.toString()

        if (companyGeneral.logo.isNotEmpty()) {
            Picasso.get()
                .load(companyGeneral.logo)
                .resize(800, 800)
                .transform(
                    RoundedCornersTransformation(
                        15,
                        0,
                        RoundedCornersTransformation.CornerType.ALL
                    )
                )
                .into(binding.companyLogo)
        }else{
            Picasso.get().load(R.drawable.no_image_available)
                .resize(800, 800)
                .transform(
                    RoundedCornersTransformation(
                        15,
                        0,
                        RoundedCornersTransformation.CornerType.ALL
                    )
                ).into(binding.companyLogo)
        }

    }

}