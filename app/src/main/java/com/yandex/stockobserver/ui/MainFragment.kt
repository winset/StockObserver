package com.yandex.stockobserver.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.yandex.stockobserver.MainViewModel
import com.yandex.stockobserver.R
import com.yandex.stockobserver.ui.adapter.CompaniesPagerAdapter
import com.yandex.stockobserver.ui.adapter.TopWatchedAdapter
import com.yandex.stockobserver.databinding.MainFragmentBinding
import com.yandex.stockobserver.genralInfo.CompanyInfo

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding

    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private val stocksAdapter = TopWatchedAdapter(::onItemClick, ::onFavoriteClick)
    private val favouriteAdapter = TopWatchedAdapter(::onItemClick, ::onFavoriteClick)
    private val stocksRecycler by lazy { RecyclerView(requireContext()) }
    private val favouriteRecycler by lazy { RecyclerView(requireContext()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPager()
        setTopHoldings()
        setFavouriteHoldings()
    }

    private fun initPager() {
        val recyclers = mutableListOf<RecyclerView>()
        recyclers.add(stocksRecycler)
        recyclers.add(favouriteRecycler)

        val pagerAdapter = CompaniesPagerAdapter(recyclers)
        binding.companiesPager.adapter = pagerAdapter
        //for tv animation
        binding.companiesPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position==0){
                    binding.stocksBtn.apply {
                            setTextSize(TypedValue.COMPLEX_UNIT_SP,28F)
                            setTextColor(context.resources.getColor(R.color.black_text))
                    }
                    binding.favouriteBtn.apply {
                        setTextSize(TypedValue.COMPLEX_UNIT_SP,18F)
                        setTextColor(context.resources.getColor(R.color.gray_text))
                    }
                }
                if (position==1){
                    binding.stocksBtn.apply {
                        setTextSize(TypedValue.COMPLEX_UNIT_SP,18F)
                        setTextColor(context.resources.getColor(R.color.gray_text))
                    }
                    binding.favouriteBtn.apply {
                        setTextSize(TypedValue.COMPLEX_UNIT_SP,28F)
                        setTextColor(context.resources.getColor(R.color.black_text))
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    private fun recyclerScrollListener(): RecyclerView.OnScrollListener {
        var pastVisiblesItems: Int
        var visibleItemCount: Int
        var totalItemCount: Int
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    visibleItemCount = recyclerView.layoutManager?.childCount!!
                    totalItemCount = recyclerView.layoutManager?.itemCount!!
                    pastVisiblesItems =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    viewModel.loadOnScroll(
                        pastVisiblesItems,
                        visibleItemCount,
                        totalItemCount
                    )
                }
            }
        }
        return scrollListener
    }

    private fun setTopHoldings() {
        stocksRecycler.apply {
            adapter = stocksAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(recyclerScrollListener())
        }
        viewModel.vooCompanies.observe(this, Observer {
            stocksAdapter.updateData(it)
        })
    }

    private fun setFavouriteHoldings(){
        favouriteRecycler.apply {
            adapter = favouriteAdapter
            layoutManager = LinearLayoutManager(context)
        }
        viewModel.favouriteCompanies.observe(this, Observer {
            favouriteAdapter.updateData(it)
        })
    }

    private fun onItemClick(cusip: String) {
        viewModel.onItemClick(cusip)
    }

    private fun onFavoriteClick(companyInfo: CompanyInfo) {
        viewModel.onFavoriteClick(companyInfo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}