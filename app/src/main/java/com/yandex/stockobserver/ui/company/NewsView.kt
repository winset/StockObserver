package com.yandex.stockobserver.ui.company

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yandex.stockobserver.databinding.NewsViewBinding
import com.yandex.stockobserver.genralInfo.CompanyNewsItem
import com.yandex.stockobserver.ui.adapter.NewsAdapter


class NewsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defAttributeSet: Int = 0
) : LinearLayout(
    context, attrs, defAttributeSet
) {
    private val TAG: String = "NewsView"
    private var binding:NewsViewBinding =
        NewsViewBinding.inflate(LayoutInflater.from(context), this)
    private val newsAdapter = NewsAdapter(::onItemClick)
    private lateinit var viewModel: CompanyViewModel
    private lateinit var navController: NavController

    private fun onItemClick(news: CompanyNewsItem) {
        Log.d(TAG, "onItemClick: ${news.url}")
        val directions = CompanyFragmentDirections.actionCompanyFragmentToWebViewFragment(news.url)
        navController.navigate(directions)
    }

     fun createView(viewModel: CompanyViewModel,navController: NavController) {
         this.viewModel = viewModel
         this.navController = navController
         initRecycler()
    }

    private fun initRecycler(){
        binding.newsRv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addOnScrollListener(recyclerScrollListener())
        }
    }

    fun addDataToRecycler(news: List<CompanyNewsItem>){
        newsAdapter.updateData(newsList = news)
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
                    if (::viewModel.isInitialized){
                        viewModel.loadMoreNews(
                            pastVisiblesItems,
                            visibleItemCount,
                            totalItemCount
                        )
                    }
                }
            }
        }
        return scrollListener
    }
}