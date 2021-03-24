package com.yandex.stockobserver.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yandex.stockobserver.R
import com.yandex.stockobserver.genralInfo.Hint

class HintHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun create(parent: ViewGroup): HintHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.hint_item, parent, false)
            return HintHolder(view)
        }
    }

    private val hintTV: TextView = itemView.findViewById(R.id.hint_tv)

    fun bind(hint: Hint) {
        hintTV.text = hint.hint
    }
}