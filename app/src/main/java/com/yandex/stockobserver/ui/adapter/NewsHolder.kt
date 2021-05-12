package com.yandex.stockobserver.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yandex.stockobserver.R
import com.yandex.stockobserver.model.CompanyNewsItem
import com.yandex.stockobserver.utils.convertFromUnixToDateString
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation


class NewsHolder(
    view: View,
    private val onItemClick: (CompanyNewsItem) -> Unit
) : RecyclerView.ViewHolder(view) {
    private val layout: RelativeLayout = itemView.findViewById(R.id.news_item_layout)
    private val headline:TextView = itemView.findViewById(R.id.headline)
    private val image:ImageView = itemView.findViewById(R.id.image_news)
    private val source:TextView = itemView.findViewById(R.id.source)
    private val data:TextView = itemView.findViewById(R.id.date)

    fun bind(newsItem: CompanyNewsItem){
        headline.text = newsItem.headline
        source.text = newsItem.source
        data.text = convertFromUnixToDateString(newsItem.datetime)
        if (newsItem.image.isNotEmpty()) {
            Picasso.get()
                .load(newsItem.image)
                .resize(800, 800)
                .transform(
                    RoundedCornersTransformation(
                        15,
                        0,
                        RoundedCornersTransformation.CornerType.ALL
                    )
                )
                .into(image)
        }else{
            image.visibility = View.GONE
        }
        layout.setOnClickListener {
           onItemClick(newsItem)
        }
    }

    fun changeBackground(position: Int) {
        if (position % 2 != 0) {
            itemView.setBackgroundColor(itemView.resources.getColor(R.color.white))
        } else {
            layout.background = itemView.resources.getDrawable(R.drawable.rounded_background)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClick: (CompanyNewsItem) -> Unit
        ): NewsHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.news_item, parent, false)
            return NewsHolder(view, onItemClick)
        }
    }
}