package com.yandex.stockobserver

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yandex.stockobserver.adapter.CompaniesPagerAdapter
import com.yandex.stockobserver.adapter.TopWatchedAdapter
import com.yandex.stockobserver.databinding.MainFragmentBinding

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

    }

    private fun setTopHoldings() {
        stocksRecycler.apply {
            adapter = stocksAdapter
            layoutManager = LinearLayoutManager(context)
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

    private fun onFavoriteClick(cusip: Int) {
        viewModel.onFavoriteClick(cusip)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}