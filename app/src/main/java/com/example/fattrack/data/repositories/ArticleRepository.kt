package com.example.fattrack.data.repositories

import com.example.fattrack.services.retrofit.ApiService

class ArticleRepository (private val apiService: ApiService){

    companion object {
        fun getInstance (apiService: ApiService): ArticleRepository {
            return ArticleRepository(apiService)
        }
    }

}