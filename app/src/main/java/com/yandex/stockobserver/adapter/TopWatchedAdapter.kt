package com.yandex.stockobserver.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yandex.stockobserver.R
import com.yandex.stockobserver.genralInfo.CompanyInfo

class TopWatchedAdapter(private val onItemClick: (String) -> Unit,private val onFavoriteClick: (Int) -> Unit) :
    RecyclerView.Adapter<TopWatchedHolder>() {

    private var topList = mutableListOf<CompanyInfo>()
    private var coloId:Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopWatchedHolder {
        return TopWatchedHolder.create(parent, onItemClick,onFavoriteClick)
    }

    override fun onBindViewHolder(holder: TopWatchedHolder, position: Int) {
        holder.changeBackground(position)
        holder.bind(topList[position])
    }

    override fun getItemCount(): Int {
        return topList.size
    }

    fun updateData(topList: List<CompanyInfo>) {
        val diffResult = DiffUtil.calculateDiff(CompanyDiffUtilCallback(this.topList, topList))
        this.topList.clear()
        this.topList.addAll(topList)
        diffResult.dispatchUpdatesTo(this)
    }
}