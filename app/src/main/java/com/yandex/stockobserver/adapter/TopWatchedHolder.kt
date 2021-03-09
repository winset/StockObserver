package com.yandex.stockobserver.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.yandex.stockobserver.R
import com.yandex.stockobserver.genralInfo.CompanyInfo

class TopWatchedHolder(view: View, val onItemClick: (String) -> Unit, val onFavoriteClick: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
    private val layout:ConstraintLayout = itemView.findViewById(R.id.top_background)
    private val name:TextView = itemView.findViewById(R.id.company_name)
    private val symbol:TextView = itemView.findViewById(R.id.company_symbol)
    private val favorite:ImageView = itemView.findViewById(R.id.favorite_btn)

    companion object {
        fun create(parent: ViewGroup, onItemClick: (String) -> Unit,onFavoriteClick: (Int) -> Unit): TopWatchedHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.top_watched_item, parent, false)
            return TopWatchedHolder(view, onItemClick,onFavoriteClick)
        }
    }

    fun bind(item: CompanyInfo) {
        name.text = item.name
        symbol.text = item.symbol

        itemView.setOnClickListener {
            onItemClick(item.cusip!!)
        }

        favorite.setOnClickListener {
            onFavoriteClick
        }

    }

    fun changeBackground(position:Int){
        if (position%2 != 0) {
            itemView.setBackgroundColor(itemView.resources.getColor(R.color.white))
        }else{
            layout.background = itemView.resources.getDrawable(R.drawable.rounded_background)
        }

    }
}