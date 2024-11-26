package com.example.fattrack.data.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fattrack.data.repositories.ArticleRepository

class ArticlesViewModel(private val articleRepository: ArticleRepository) : ViewModel() {

    suspend fun getArticles() = articleRepository.getListArticles()
}
