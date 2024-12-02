package com.example.fattrack.view.detail

import android.os.Bundle
import com.bumptech.glide.Glide
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.fattrack.data.ViewModelFactory
import com.example.fattrack.data.viewmodel.DetailViewModel
import com.example.fattrack.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    companion object {
        const val ARTICLE_ID = "article_id" // Konstanta untuk kunci data
    }

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val articleId = intent.getStringExtra(ARTICLE_ID)

        if (articleId != null) {
            // Memuat detail artikel berdasarkan ID
            loadArticleDetail(articleId)
        } else {
            Toast.makeText(this, "Article ID is missing!", Toast.LENGTH_SHORT).show()
            finish() // Kembali jika ID tidak ditemukan
        }

    }

    private fun loadArticleDetail(articleId: String) {
        // Panggil ViewModel untuk memuat detail artikel
        viewModel.fetchArticleDetail(articleId)

        // Amati data detail artikel
        viewModel.articleDetail.observe(this) { article ->
            if (article != null) {
                // Update UI dengan data artikel menggunakan binding
                binding.tvArticleTitle.text = article.title
                binding.tvArticleName.text = article.author ?: "Unknown Author"
                binding.tvArticleDate.text = article.date ?: "Unknown Date"
                binding.tvEventDescription.text = article.description

                Glide.with(this@DetailActivity)
                    .load(article.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivEventImage)
            } else {
                Toast.makeText(this, "Failed to load article details.", Toast.LENGTH_SHORT).show()
            }
        }

        // Amati status loading
        viewModel.isLoading.observe(this) { isLoading ->
            // Handle loading state, misalnya tampilkan progress bar
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Amati pesan error
        viewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}