package com.yandex.stockobserver.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter

class CompaniesPagerAdapter(private val recyclers: List<RecyclerView>) : PagerAdapter() {

    override fun getCount(): Int {
        return recyclers.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val recycler: View = recyclers[position]
        container.addView(recycler)
        return recycler
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}