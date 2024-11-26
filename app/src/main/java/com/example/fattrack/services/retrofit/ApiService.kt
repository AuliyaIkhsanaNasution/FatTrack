package com.example.fattrack.services.retrofit

import com.example.fattrack.services.responses.ResponseArticle
import retrofit2.http.GET

interface ApiService {

    @GET("articles")
    suspend fun getArticles(): ResponseArticle

}
