package com.yandex.stockobserver.ui.company

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yandex.stockobserver.databinding.NewsViewBinding
import com.yandex.stockobserver.model.CompanyNewsItem
import com.yandex.stockobserver.ui.adapter.NewsAdapter


class NewsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defAttributeSet: Int = 0
) : LinearLayout(
    context, attrs, defAttributeSet
) {
    private val TAG: String = "NewsView"
    private var binding: NewsViewBinding =
        NewsViewBinding.inflate(LayoutInflater.from(context), this)
    private val newsAdapter = NewsAdapter(::onItemClick)
    private lateinit var viewModel: CompanyViewModel
    private lateinit var navController: NavController

    private fun onItemClick(news: CompanyNewsItem) {
        Log.d(TAG, "onItemClick: ${news.url}")
        val directions = CompanyFragmentDirections.actionCompanyFragmentToWebViewFragment(news.url)
        navController.navigate(directions)
    }

    fun createView(viewModel: CompanyViewModel, navController: NavController) {
        this.viewModel = viewModel
        this.navController = navController
        initRecycler()
    }

    private fun initRecycler() {
        binding.newsRv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun addDataToRecycler(news: List<CompanyNewsItem>) {
        newsAdapter.updateData(newsList = news)
    }

    fun showNoNewsMsg() {
        Log.d(TAG, "showNoNewsMsg: ")
        binding.newsRv.visibility = View.GONE
        binding.textView.visibility = View.VISIBLE
    }

}