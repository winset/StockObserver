package com.yandex.stockobserver.ui

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.yandex.stockobserver.MainViewModel
import com.yandex.stockobserver.R
import com.yandex.stockobserver.databinding.MainFragmentBinding
import com.yandex.stockobserver.genralInfo.CompanyInfo
import com.yandex.stockobserver.ui.adapter.CompaniesPagerAdapter
import com.yandex.stockobserver.ui.adapter.HintAdapter
import com.yandex.stockobserver.ui.adapter.TopWatchedAdapter


class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding

    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private val stocksAdapter = TopWatchedAdapter(::onItemClick, ::onFavoriteClick)
    private val favouriteAdapter = TopWatchedAdapter(::onItemClick, ::onFavoriteClick)
    private val searchAdapter = TopWatchedAdapter(::onItemClick, ::onFavoriteClick)
    private val stocksRecycler by lazy { RecyclerView(requireContext()) }
    private val favouriteRecycler by lazy { RecyclerView(requireContext()) }
    private val popularHintAdapter = HintAdapter()
    private val lookingHintAdapter = HintAdapter()
    /* private val popularHintRecycler by lazy { RecyclerView(requireContext()) }
     private val lookingHintRecycler by lazy { RecyclerView(requireContext()) }*/


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
        initSearchView()
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
                if (position == 0) {
                    binding.stocksBtn.apply {
                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 28F)
                        setTextColor(context.resources.getColor(R.color.black_text))
                    }
                    binding.favouriteBtn.apply {
                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
                        setTextColor(context.resources.getColor(R.color.gray_text))
                    }
                }
                if (position == 1) {
                    binding.stocksBtn.apply {
                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
                        setTextColor(context.resources.getColor(R.color.gray_text))
                    }
                    binding.favouriteBtn.apply {
                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 28F)
                        setTextColor(context.resources.getColor(R.color.black_text))
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    private fun initSearchView() {
        var searchImageView: ImageView? = null
        binding.searchView.setOnQueryTextFocusChangeListener { view, b ->
            Log.d("TAG", "initSearchView: " + b)
            Log.d("TAG", "initSearchView: " + view.isEnabled)

            if (b && view.isEnabled()) {
                initPopularHint()
                initLookingHint()

                binding.searchFeature.visibility = View.VISIBLE
                val searchImgId = resources.getIdentifier("android:id/search_mag_icon", null, null)
                searchImageView = binding.searchView.findViewById(searchImgId)
                if (searchImageView != null) {
                    searchImageView!!.setImageDrawable(resources.getDrawable(R.drawable.ic_back))
                    searchImageView!!.setOnClickListener {
                        binding.searchView.clearFocus()
                    }
                }
            } else {
                searchImageView!!.setImageDrawable(resources.getDrawable(R.drawable.ic_search))
                binding.searchFeature.visibility = View.GONE
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                // binding.searchView.clearFocus()
                if (!p0.isNullOrEmpty()) {
                    viewModel.search(p0)
                    viewModel.addLookingForHint(p0)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })

    }

    private fun initPopularHint() {
        binding.popularList.apply {
            adapter = popularHintAdapter
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL)
        }
        viewModel.popularHint.observe(this, Observer {
            popularHintAdapter.updateData(it)
        })
    }

    private fun initLookingHint() {
        binding.lookingForList.apply {
            adapter = lookingHintAdapter
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL)
        }
        viewModel.lookingHint.observe(this, Observer {
            lookingHintAdapter.updateData(it)
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

    private fun setFavouriteHoldings() {
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

    private fun onFavoriteClick(
        companyInfo: CompanyInfo,
        index: Int,
        isFavorite: Boolean,
        hashCode: Int
    ) {
        var contentType: String = ""
        if (favouriteAdapter.hashCode() == hashCode) {
            contentType = FAVORITE
        } else contentType = TOP_STOCKS

        viewModel.onFavoriteClick(companyInfo, index, isFavorite, contentType)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        const val FAVORITE = "FAVORITE"
        const val TOP_STOCKS = "TOP_STOCKS"
    }
}