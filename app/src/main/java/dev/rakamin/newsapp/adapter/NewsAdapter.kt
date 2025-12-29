package dev.rakamin.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.rakamin.newsapp.R
import dev.rakamin.newsapp.model.Article

class NewsAdapter(private val articles: MutableList<Article>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tv_title)
        val author: TextView = view.findViewById(R.id.tv_author)
        val date: TextView = view.findViewById(R.id.tv_date)
        val image: ImageView = view.findViewById(R.id.iv_news_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        holder.title.text = article.title
        holder.author.text = article.author ?: "Unknown"
        holder.date.text = article.publishedAt

        // Memuat gambar dari URL menggunakan Glide
        Glide.with(holder.itemView.context)
            .load(article.urlToImage)
            .into(holder.image)
    }

    override fun getItemCount(): Int = articles.size


    fun addData(newArticles: List<Article>) {
        val sizeBefore = articles.size
        articles.addAll(newArticles)
        notifyItemRangeInserted(sizeBefore, newArticles.size)
    }
}