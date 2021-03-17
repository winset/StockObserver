package com.yandex.stockobserver.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yandex.stockobserver.R
import com.yandex.stockobserver.genralInfo.CompanyInfo
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class TopWatchedHolder(
    view: View,
    val onItemClick: (String) -> Unit,
    val onFavoriteClick: (CompanyInfo,Int,Boolean,Int) -> Unit,
    val adapterHashCode:Int
) : RecyclerView.ViewHolder(view) {
    private val layout: ConstraintLayout = itemView.findViewById(R.id.top_background)
    private val name: TextView = itemView.findViewById(R.id.company_name)
    private val symbol: TextView = itemView.findViewById(R.id.company_symbol)
    private val logo: ImageView = itemView.findViewById(R.id.company_logo)
    private val favorite: ImageView = itemView.findViewById(R.id.favorite_btn)
    private val price: TextView = itemView.findViewById(R.id.price)
    private val delta: TextView = itemView.findViewById(R.id.delta)

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClick: (String) -> Unit,
            onFavoriteClick: (CompanyInfo,Int,Boolean,Int) -> Unit,
            adapterHashCode:Int
        ): TopWatchedHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.top_watched_item, parent, false)
            return TopWatchedHolder(view, onItemClick, onFavoriteClick,adapterHashCode)
        }
    }

    fun bind(item: CompanyInfo) {
        name.text = item.name
        symbol.text = item.symbol
        price.text = "$" + item.price

        itemView.setOnClickListener {
            onItemClick(item.cusip)
        }

        favorite.isSelected = item.isFavorite

        favorite.setOnClickListener {
            favorite.isSelected = !favorite.isSelected
            onFavoriteClick(item,adapterPosition,favorite.isSelected,adapterHashCode)
        }

        if (item.margin > 0) {
            delta.setTextColor(itemView.resources.getColor(R.color.green_text))
            delta.text = "+" + item.margin
        } else {
            delta.setTextColor(itemView.resources.getColor(R.color.red_text))
            delta.text = item.margin.toString()
        }


        if (item.logo.isNotEmpty()) {
            Picasso.get()
                .load(item.logo)
                .transform(
                    RoundedCornersTransformation(
                        15,
                        0,
                        RoundedCornersTransformation.CornerType.ALL
                    )
                )
                .into(logo)
        }
    }

    fun changeBackground(position: Int) {
        if (position % 2 != 0) {
            itemView.setBackgroundColor(itemView.resources.getColor(R.color.white))
        } else {
            layout.background = itemView.resources.getDrawable(R.drawable.rounded_background)
        }

    }
}