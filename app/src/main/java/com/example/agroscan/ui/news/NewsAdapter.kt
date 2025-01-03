package com.example.agroscan.ui.news

import android.content.Intent
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
import com.example.agroscan.ui.news_detail.DetailNewsActivity

class NewsAdapter(private val newsList: List<ArticleItem>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_news)
        val title: TextView = itemView.findViewById(R.id.tv_news_title)
        val date: TextView = itemView.findViewById(R.id.tv_news_date)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_list, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int = newsList.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.title.text = news.title
        holder.date.text = news.date_upload

        // Load image
        Glide.with(holder.itemView.context)
            .load(news.image)
            .transform(RoundedCorners(30))
            .into(holder.image)

        // Handle item click
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailNewsActivity::class.java)
            intent.putExtra("title", news.title)
            intent.putExtra("image", news.image)
            intent.putExtra("date", news.date_upload)
            holder.itemView.context.startActivity(intent)
        }
    }
}