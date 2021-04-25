package com.yandex.stockobserver.ui.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yandex.stockobserver.R
import com.yandex.stockobserver.StockApplication
import com.yandex.stockobserver.databinding.CompanyFragmentBinding
import com.yandex.stockobserver.di.injectViewModel
import com.yandex.stockobserver.ui.adapter.CompanyPagerAdapter
import com.yandex.stockobserver.ui.main.MainViewModel
import javax.inject.Inject


class CompanyFragment : Fragment() {
    private lateinit var binding: CompanyFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: CompanyViewModel by lazy { injectViewModel(viewModelFactory) }

    private val chartView by lazy { ChartView(requireContext()) }
    private val summaryView by lazy { SummaryView(requireContext()) }
    private val newsView by lazy { NewsView(requireContext()) }
    private val forecastView by lazy { ForecastView(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        StockApplication.stockComponent.inject(this)
        binding = CompanyFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerInit()
        tabLayoutInit()
        backButton()
        initChartView(chartView)
    }

    private fun getInitInfo() {

    }

    private fun viewPagerInit() {
        val views: MutableList<View> = mutableListOf()
        views.add(chartView)
        views.add(summaryView)
        views.add(newsView)
        views.add(forecastView)
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

    private fun backButton() {
        binding.backBtnFg.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun initChartView(chartView: ChartView) {
        viewModel.stockCandle.observe(viewLifecycleOwner, Observer {
            chartView.createView(it)
        })
    }

}