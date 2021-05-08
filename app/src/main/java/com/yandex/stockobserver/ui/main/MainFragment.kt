package com.yandex.stockobserver.ui.main


import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.yandex.stockobserver.R
import com.yandex.stockobserver.StockApplication
import com.yandex.stockobserver.databinding.MainFragmentBinding
import com.yandex.stockobserver.di.injectViewModel
import com.yandex.stockobserver.genralInfo.CompanyInfo
import com.yandex.stockobserver.ui.adapter.HintAdapter
import com.yandex.stockobserver.ui.adapter.StockAdapter
import com.yandex.stockobserver.ui.adapter.StoksPagerAdapter
import com.yandex.stockobserver.ui.company.CompanyFragmentArgs
import javax.inject.Inject


class MainFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding
    private lateinit var navController: NavController
    private val args by navArgs<MainFragmentArgs>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainViewModel by navGraphViewModels (R.id.nav_graph) {viewModelFactory} //lazy { injectViewModel(viewModelFactory) }
    private val stocksAdapter = StockAdapter(::onItemClick, ::onFavoriteClick)
    private val favouriteAdapter = StockAdapter(::onItemClick, ::onFavoriteClick)
    private val searchAdapter = StockAdapter(::onItemClick, ::onFavoriteClick)
    private val stocksRecycler by lazy { RecyclerView(requireContext()) }
    private val favouriteRecycler by lazy { RecyclerView(requireContext()) }
    private val popularHintAdapter = HintAdapter(::onHintClick)
    private val lookingHintAdapter = HintAdapter(::onHintClick)

    private var errorDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // viewModel = injectViewModel(viewModelFactory)
        StockApplication.stockComponent.inject(this)
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        initPager()
        setTopHoldings()
        setFavouriteHoldings()
        initSearchView()
        setSearchHoldings()
        showError()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
      //  outState.putBundle("nav_state", findNavController().saveState())
       /* if (args.isNeedToUpdate){
            Log.d("123", "onSaveInstanceState: ")
            viewModel.updateFavoriteInVooList(args.symbol, args.isFavorite)
            viewModel.getFavorites()
        }*/
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
          //  findNavController().restoreState(savedInstanceState.getBundle("nav_state"))
        }
    }

    private fun initPager() {
        val recyclers = mutableListOf<RecyclerView>()
        recyclers.add(stocksRecycler)
        recyclers.add(favouriteRecycler)

        val pagerAdapter = StoksPagerAdapter(recyclers)
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
        binding.favouriteBtn.setOnClickListener {
            val currentPosotion = binding.companiesPager.currentItem
            if (currentPosotion == 0) {
                binding.companiesPager.currentItem = 1
            }
        }

        binding.stocksBtn.setOnClickListener {
            val currentPosotion = binding.companiesPager.currentItem
            if (currentPosotion == 1) {
                binding.companiesPager.currentItem = 0
            }
        }

    }

    private fun initSearchView() {
        var searchImageView: ImageView? = null
        var closeImageView: ImageView? = null
        binding.searchView.setOnQueryTextFocusChangeListener { view, b ->

            if (b && view.isEnabled()) {
                initPopularHint()
                initLookingHint()

                binding.searchFeature.visibility = View.VISIBLE
                val searchImgId = resources.getIdentifier("android:id/search_mag_icon", null, null)
                val searchCloseButtonId: Int =
                    resources.getIdentifier("android:id/search_close_btn", null, null)
                closeImageView = binding.searchView.findViewById(searchCloseButtonId)
                searchImageView = binding.searchView.findViewById(searchImgId)
                if (searchImageView != null) {
                    searchImageView!!.setImageDrawable(resources.getDrawable(R.drawable.ic_back))
                    searchImageView!!.setOnClickListener {
                        binding.searchView.isIconified = true
                        binding.searchView.clearFocus()
                        binding.searchCompanies.visibility = View.GONE
                        binding.companiesPager.visibility = View.VISIBLE
                    }

                    if (closeImageView != null) {
                        closeImageView!!.setOnClickListener {
                            binding.searchView.isIconified = true
                            binding.searchView.clearFocus()
                        }
                    }
                }
            } else {
                searchImageView!!.setImageDrawable(resources.getDrawable(R.drawable.ic_search))
                binding.searchFeature.visibility = View.GONE
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (!p0.isNullOrEmpty()) {
                    viewModel.search(p0)
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
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        }
        viewModel.popularHint.observe(viewLifecycleOwner, Observer {
            popularHintAdapter.updateData(it)
        })
    }

    private fun initLookingHint() {
        binding.lookingForList.apply {
            adapter = lookingHintAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        }
        viewModel.lookingHint.observe(viewLifecycleOwner, Observer {
            lookingHintAdapter.updateData(it)
        })
        viewModel.isHaveUserHint.observe(viewLifecycleOwner, Observer {
            if (it){
                binding.lookingForText.visibility = View.VISIBLE
                binding.lookingForList.visibility = View.VISIBLE
            }else{
                binding.lookingForText.visibility = View.GONE
                binding.lookingForList.visibility = View.GONE
            }
        })
    }

    private fun recyclerScrollListener(contentType: String): RecyclerView.OnScrollListener {
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
                        totalItemCount,
                        contentType
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
            addOnScrollListener(recyclerScrollListener(TOP_STOCKS))
        }
        viewModel.vooCompanies.observe(viewLifecycleOwner, Observer {
            stocksAdapter.updateData(it)
        })
    }

    private fun setFavouriteHoldings() {
        favouriteRecycler.apply {
            adapter = favouriteAdapter
            layoutManager = LinearLayoutManager(context)
        }
        viewModel.favouriteCompanies.observe(viewLifecycleOwner, Observer {
            favouriteAdapter.updateData(it)
        })
    }

    private fun setSearchHoldings() {
        binding.searchCompanies.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(recyclerScrollListener(SIMILAR_SEARCH))
        }

        viewModel.searchCompanies.observe(viewLifecycleOwner, Observer {
            binding.companiesPager.visibility = View.GONE
            binding.searchCompanies.visibility = View.VISIBLE
            Log.d("TAG", "setSearchHoldings: " + it.first().name)
            searchAdapter.updateData(it)
        })
    }

    private fun onItemClick(companyInfo: CompanyInfo) {
        viewModel.onItemClick(companyInfo.symbol)
        val directions = MainFragmentDirections.actionMainFragmentToCompanyFragment(companyInfo)
        activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
        findNavController().navigate(directions)

    }

    private fun onHintClick(hint: String) {
        binding.searchView.setQuery(hint, true)
    }

    private fun onFavoriteClick(
        companyInfo: CompanyInfo,
        index: Int,
        isFavorite: Boolean,
        hashCode: Int
    ) {
        var contentType = ""
        if (stocksAdapter.hashCode() == hashCode) {
            contentType = TOP_STOCKS
        } else contentType = FAVORITE

        viewModel.onFavoriteClick(companyInfo, index, isFavorite, contentType)
    }

    private fun showError() {
        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (errorDialog == null) {
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setTitle("Error")
                    //.setMessage(resources.getString(it))
                    .setMessage(it)
                    .setPositiveButton(
                        "Ok"
                    ) { alertDialog, id ->
                        errorDialog = null
                        alertDialog.cancel()
                    }.setOnDismissListener {
                        errorDialog = null
                    }
                errorDialog = alertDialog.create()
                errorDialog!!.show()
            }
        })
    }

    override fun onDestroyView() {
        Log.d("AAAA123", "onDestroyView: ")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d("AAAA123", "onDestroy: ")
        super.onDestroy()
    }

    companion object {
        const val FAVORITE = "FAVORITE"
        const val TOP_STOCKS = "TOP_STOCKS"
        const val SIMILAR_SEARCH = "SIMILAR_SEARCH"
    }
}