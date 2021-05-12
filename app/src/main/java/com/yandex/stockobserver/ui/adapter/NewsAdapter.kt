package com.yandex.stockobserver.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yandex.stockobserver.model.CompanyNewsItem

class NewsAdapter(private val onItemClick: (CompanyNewsItem) -> Unit) :
    RecyclerView.Adapter<NewsHolder>()  {
    private var newsList = mutableListOf<CompanyNewsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        return NewsHolder.create(parent, onItemClick)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        holder.changeBackground(position)
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun updateData(newsList: List<CompanyNewsItem>) {
        val diffResult = DiffUtil.calculateDiff(NewsDiffUtilCallback(this.newsList, newsList))
        this.newsList.clear()
        this.newsList.addAll(newsList)
        diffResult.dispatchUpdatesTo(this)
    }

}