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
import com.yandex.stockobserver.model.CompanyInfo
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class StockHolder(
    view: View,
    private val onItemClick: (CompanyInfo) -> Unit,
    private val onFavoriteClick: (CompanyInfo, Int, Boolean, Int) -> Unit,
    private val adapterHashCode: Int
) : RecyclerView.ViewHolder(view) {
    private val layout: ConstraintLayout = itemView.findViewById(R.id.top_background)
    private val name: TextView = itemView.findViewById(R.id.company_name_fg)
    private val symbol: TextView = itemView.findViewById(R.id.company_symbol_fg)
    private val logo: ImageView = itemView.findViewById(R.id.company_logo)
    private val favorite: ImageView = itemView.findViewById(R.id.favorite_btn)
    private val price: TextView = itemView.findViewById(R.id.price)
    private val delta: TextView = itemView.findViewById(R.id.delta)

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClick: (CompanyInfo) -> Unit,
            onFavoriteClick: (CompanyInfo, Int, Boolean, Int) -> Unit,
            adapterHashCode: Int
        ): StockHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.stock_item, parent, false)
            return StockHolder(view, onItemClick, onFavoriteClick, adapterHashCode)
        }
    }

    fun bind(item: CompanyInfo) {
        name.text = item.name
        symbol.text = item.symbol
        price.text = "$" + item.price

        itemView.setOnClickListener {
            onItemClick(item)
        }

        favorite.isSelected = item.isFavorite

        favorite.setOnClickListener {
            favorite.isSelected = !favorite.isSelected
            onFavoriteClick(item, adapterPosition, favorite.isSelected, adapterHashCode)
        }

        if ((Math.round((item.price - item.prevClosePrice)*100).toDouble()/100) > 0) {
            delta.setTextColor(itemView.resources.getColor(R.color.green_text))
        } else {
            delta.setTextColor(itemView.resources.getColor(R.color.red_text))
        }
        delta.text = item.margin

        if (item.logo.isNotEmpty()) {
            Picasso.get()
                .load(item.logo)
                .resize(100, 100)
                .transform(
                    RoundedCornersTransformation(
                        15,
                        0,
                        RoundedCornersTransformation.CornerType.ALL
                    )
                )
                .into(logo)
        }else{
            Picasso.get().load(R.drawable.no_image_available)
                .resize(100, 100)
                .transform(
                    RoundedCornersTransformation(
                        15,
                        0,
                        RoundedCornersTransformation.CornerType.ALL
                    )
                ).into(logo)
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