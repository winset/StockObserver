package com.yandex.stockobserver.adapter

import androidx.recyclerview.widget.DiffUtil
import com.yandex.stockobserver.genralInfo.CompanyInfo

class CompanyDiffUtilCallback(
    private val oldCompanies: List<CompanyInfo>,
    private val newCompanies: List<CompanyInfo>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldCompanies.size
    }

    override fun getNewListSize(): Int {
        return newCompanies.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCompanies = oldCompanies[oldItemPosition]
        val newCompanies = newCompanies[newItemPosition]
        return oldCompanies.cusip == newCompanies.cusip
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCompanies = oldCompanies[oldItemPosition]
        val newCompanies = newCompanies[newItemPosition]
        return oldCompanies == newCompanies
    }
}