package com.yandex.stockobserver.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yandex.stockobserver.genralInfo.CompanyInfo
import com.yandex.stockobserver.genralInfo.Hint

class HintAdapter(): RecyclerView.Adapter<HintHolder>() {

    private var hintList = mutableListOf<Hint>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HintHolder {
       return HintHolder.create(parent)
    }

    override fun onBindViewHolder(holder: HintHolder, position: Int) {
       holder.bind(hintList[position])
    }

    override fun getItemCount(): Int {
        return hintList.size
    }

    fun updateData(hintList: List<Hint>) {
        val diffResult = DiffUtil.calculateDiff(HintDiffUtilCallback(this.hintList, hintList))
        this.hintList.clear()
        this.hintList.addAll(hintList)
        diffResult.dispatchUpdatesTo(this)
    }

}