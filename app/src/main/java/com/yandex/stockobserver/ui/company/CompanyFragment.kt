package com.yandex.stockobserver.ui.company

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yandex.stockobserver.R
import com.yandex.stockobserver.StockApplication
import com.yandex.stockobserver.databinding.CompanyFragmentBinding
import com.yandex.stockobserver.di.injectViewModel
import com.yandex.stockobserver.ui.adapter.CompanyPagerAdapter
import javax.inject.Inject


class CompanyFragment : Fragment() {
    private lateinit var binding: CompanyFragmentBinding
    private val args by navArgs<CompanyFragmentArgs>()
    private lateinit var navController: NavController

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
        navController = findNavController()
        StockApplication.stockComponent.inject(this)
        viewModel.setGeneralInfo(args.companyInformation)
        binding = CompanyFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerInit()
        tabLayoutInit()
        backButton()
        initChartView(chartView)
        initCompanySymbol(args.companyInformation.symbol)
        initCompanyName(args.companyInformation.name)
        initNews(newsView)
        initIsFavorite()
    }

    private fun getInitInfo() {

    }

    private fun initCompanySymbol(symbol: String) {
        binding.companySymbolFg.text = symbol
    }

    private fun initCompanyName(name: String) {
        binding.companyNameFg.text = name
    }

    private fun initIsFavorite() {
        binding.companyFavorite.setOnClickListener {
            viewModel.onFavoriteClick()
        }
        viewModel.isFavorite.observe(viewLifecycleOwner, Observer {
            binding.companyFavorite.isSelected = it
        })
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
            Log.d("CompanyFragment", "backButton: ")
            navigateToMain()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            Log.d("CompanyFragment", "backButton11: ")
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        viewModel.isMainNeedUpdate()
        viewModel.isMainFragmentNeedUpdate.observe(viewLifecycleOwner, Observer {
            val directions = CompanyFragmentDirections
                .actionCompanyFragmentToMainFragment(
                    args.companyInformation.symbol,
                    viewModel.isFavorite.value!!,
                    it
                )
            findNavController().navigate(directions)
        })
    }

    private fun initChartView(chartView: ChartView) {
        viewModel.stockCandle.observe(viewLifecycleOwner, Observer {
            chartView.createView(it)
        })
    }

    private fun initNews(newsView: NewsView) {
        newsView.createView(viewModel,navController)
        viewModel.news.observe(viewLifecycleOwner, Observer {
            newsView.addDataToRecycler(it)
        })
    }
}