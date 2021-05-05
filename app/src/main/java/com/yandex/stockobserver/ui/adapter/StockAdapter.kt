package com.yandex.stockobserver.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yandex.stockobserver.genralInfo.CompanyInfo

class StockAdapter(private val onItemClick: (CompanyInfo) -> Unit, private val onFavoriteClick: (CompanyInfo, Int, Boolean, Int) -> Unit) :
    RecyclerView.Adapter<StockHolder>() {

    private var topList = mutableListOf<CompanyInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockHolder {
        return StockHolder.create(parent, onItemClick,onFavoriteClick,hashCode())
    }

    override fun onBindViewHolder(holder: StockHolder, position: Int) {
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