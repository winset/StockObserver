package com.yandex.stockobserver.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.yandex.stockobserver.model.Hint

class HintDiffUtilCallback(
    private val oldHints: List<Hint>,
    private val newHints: List<Hint>
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
        return oldCompanies.hint == newCompanies.hint
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCompanies = oldHints[oldItemPosition]
        val newCompanies = newHints[newItemPosition]
        return oldCompanies == newCompanies
    }

}