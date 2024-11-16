package com.example.fattrack.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.fattrack.R
import com.example.fattrack.data.ArticleData

class ArticleAdapter(private val listArticle: ArrayList<ArticleData>) : RecyclerView.Adapter<ArticleAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_item)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listArticle.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (title, name, date, description, photo) = listArticle[position]

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(photo)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(holder.imgPhoto)

        holder.tvTitle.text = title
        holder.tvName.text = name
        holder.tvDate.text = date
        holder.tvDescription.text = description


    }

}