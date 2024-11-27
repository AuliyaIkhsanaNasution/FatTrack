package com.example.fattrack.data.services.retrofit

import com.example.fattrack.data.services.responses.ResponseArticle
import com.example.fattrack.data.services.responses.ResponseLogin
import com.example.fattrack.data.services.responses.ResponseRegister
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    //AUTH
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("email") email: String,
        @Field("nama") nama: String,
        @Field("password") password: String,
    ) : Response<ResponseRegister>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ) : Response<ResponseLogin>


    //articles
    @GET("articles")
    suspend fun getArticles(): Response<ResponseArticle>
}

