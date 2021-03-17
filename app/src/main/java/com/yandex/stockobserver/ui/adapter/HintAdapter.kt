package com.yandex.stockobserver.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yandex.stockobserver.genralInfo.CompanyInfo

class HintAdapter(): RecyclerView.Adapter<HintHolder>() {

    private var hintList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HintHolder {
       return HintHolder.create(parent)
    }

    override fun onBindViewHolder(holder: HintHolder, position: Int) {
       holder.bind(hintList[position])
    }

    override fun getItemCount(): Int {
        return hintList.size
    }

    fun updateData(hintList: List<String>) {
        val diffResult = DiffUtil.calculateDiff(HintDiffUtilCallback(this.hintList, hintList))
        this.hintList.clear()
        this.hintList.addAll(hintList)
        diffResult.dispatchUpdatesTo(this)
    }

}