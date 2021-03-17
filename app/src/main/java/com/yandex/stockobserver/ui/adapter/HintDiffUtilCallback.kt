package com.yandex.stockobserver.ui.adapter

import androidx.recyclerview.widget.DiffUtil

class HintDiffUtilCallback(
    private val oldHints: List<String>,
    private val newHints: List<String>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldHints.size
    }

    override fun getNewListSize(): Int {
        return newHints.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCompanies = oldHints[oldItemPosition]
        val newCompanies = newHints[newItemPosition]
        return oldCompanies == newCompanies
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCompanies = oldHints[oldItemPosition]
        val newCompanies = newHints[newItemPosition]
        return oldCompanies == newCompanies
    }

}