package com.example.fattrack.data.repositories

import com.example.fattrack.services.responses.ResponseArticle
import com.example.fattrack.services.retrofit.ApiService

class ArticleRepository (private val apiService: ApiService){

    suspend fun getListArticles(): Result<ResponseArticle> {
        return try {
            // Panggil API
            val response = apiService.getArticles()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Response body is null"))
                }
            } else {
                // Log error untuk debugging
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("Articles Error with code: ${response.code()}, message: $errorMessage"))
            }
        } catch (e: Exception) {
            // Tangkap semua exception lainnya
            Result.failure(e)
        }
    }


    companion object {
        fun getInstance (apiService: ApiService): ArticleRepository {
            return ArticleRepository(apiService)
        }
    }

}