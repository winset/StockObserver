package com.yandex.stockobserver.ui.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yandex.stockobserver.R
import com.yandex.stockobserver.databinding.CompanyFragmentBinding
import com.yandex.stockobserver.ui.adapter.CompanyPagerAdapter


class CompanyFragment : Fragment() {
    private lateinit var binding: CompanyFragmentBinding
    private val viewModel: CompanyViewModel by lazy { ViewModelProvider(this).get(CompanyViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CompanyFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chartView = ChartView(requireContext())
        val summaryView = SummaryView(requireContext())
        val newsView = NewsView(requireContext())
        val forecastView = ForecastView(requireContext())
        val views: MutableList<View> = mutableListOf()
        views.add(chartView)
        views.add(summaryView)
        views.add(newsView)
        views.add(forecastView)
        viewPagerInit(views)
        tabLayoutInit()
        backButton()
    }

    private fun getInitInfo() {

    }

    private fun viewPagerInit(views: List<View>) {
        val pagerAdapter = CompanyPagerAdapter(views)
        binding.detailInfoFg.adapter = pagerAdapter
    }

    private fun tabLayoutInit() {
        binding.tabsFg.setupWithViewPager(binding.detailInfoFg)
        binding.tabsFg.getTabAt(0)?.text = resources.getString(R.string.chart)
        binding.tabsFg.getTabAt(1)?.text = resources.getString(R.string.summary)
        binding.tabsFg.getTabAt(2)?.text = resources.getString(R.string.news)
        binding.tabsFg.getTabAt(3)?.text = resources.getString(R.string.forecasts)
        binding.tabsFg.tabTextColors =
            ContextCompat.getColorStateList(requireContext(), R.color.tab_selector)
    }

    private fun backButton(){
        binding.backBtnFg.setOnClickListener {
            activity?.onBackPressed()
        }
    }

}