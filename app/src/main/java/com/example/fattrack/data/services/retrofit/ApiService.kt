package com.example.fattrack.data.services.retrofit

import com.example.fattrack.data.services.responses.ResponseArticle
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("articles")
    suspend fun getArticles(): Response<ResponseArticle>
}

