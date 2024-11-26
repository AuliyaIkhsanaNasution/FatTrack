package com.example.fattrack.data.di

import com.example.fattrack.data.repositories.ArticleRepository
import com.example.fattrack.data.services.retrofit.ApiConfig

object Injection {

    fun provideArticlesRepository(): ArticleRepository {
        val apiService = ApiConfig.getApiService()
        return ArticleRepository.getInstance(apiService)
    }
}