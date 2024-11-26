package com.example.fattrack.data.di

import android.content.Context
import com.example.fattrack.data.pref.ProfilePreferences
import com.example.fattrack.data.pref.dataStore
import com.example.fattrack.data.repositories.ArticleRepository
import com.example.fattrack.data.services.retrofit.ApiConfig

object Injection {

    fun provideArticlesRepository(): ArticleRepository {
        val apiService = ApiConfig.getApiService()
        return ArticleRepository.getInstance(apiService)
    }

    fun provideProfilePreferences(context: Context): ProfilePreferences {
        return ProfilePreferences.getInstance(context.dataStore)
    }
}