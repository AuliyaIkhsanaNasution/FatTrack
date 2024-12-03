package com.example.fattrack.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fattrack.data.data.PredictionItemData
import com.example.fattrack.databinding.ItemHistoryScanBinding

class HistoryPredictAdapter(private var items: List<PredictionItemData>) :
    RecyclerView.Adapter<HistoryPredictAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemHistoryScanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: PredictionItemData) {
            binding.tvNameRiwayat.text = item.foodName
            binding.tvDateRiwayat.text = item.predictionDate
            binding.tvTotalCek.text = "${item.calories} kcal"
            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .into(binding.ivPhotoRiwayat)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryScanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newItems: List<PredictionItemData>) {
        items = newItems
        notifyDataSetChanged()
    }
}
