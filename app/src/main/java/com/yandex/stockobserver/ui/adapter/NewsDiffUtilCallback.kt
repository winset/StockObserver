package com.yandex.stockobserver.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.yandex.stockobserver.genralInfo.CompanyNewsItem

class NewsDiffUtilCallback(
    private val oldNews: List<CompanyNewsItem>,
    private val newNews: List<CompanyNewsItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldNews.size
    }

    override fun getNewListSize(): Int {
        return newNews.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCompanies = oldNews[oldItemPosition]
        val newCompanies = newNews[newItemPosition]
        return oldCompanies.id == newCompanies.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCompanies = oldNews[oldItemPosition]
        val newCompanies = newNews[newItemPosition]
        return oldCompanies == newCompanies
    }
}