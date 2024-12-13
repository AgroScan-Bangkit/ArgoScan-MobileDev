package com.example.agroscan.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.agroscan.R
import com.example.agroscan.data.local.response.ArticleItem

class ListTopArticlesAdapter(private val list: List<ArticleItem>) :
    RecyclerView.Adapter<ListTopArticlesAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTopArticlesAdapter.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_top_articles, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (title, image, date) = list[position]
        holder.itemView.findViewById<TextView>(R.id.tv_title_top_article).text = title
        holder.itemView.findViewById<TextView>(R.id.tv_date_top_article).text = date
        Glide.with(holder.itemView.context)
            .load(image)
            .transform(RoundedCorners(30))
            .into(holder.itemView.findViewById(R.id.image_top_article))

    }

    override fun getItemCount(): Int = list.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_title_top_article)
        val date: TextView = itemView.findViewById(R.id.tv_date_top_article)
        val image: ImageView = itemView.findViewById(R.id.image_top_article)
    }
}