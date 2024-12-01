package com.example.fattrack.data.services.retrofit

import com.example.fattrack.data.services.responses.ResponseArticle
import com.example.fattrack.data.services.responses.ResponseLogin
import com.example.fattrack.data.services.responses.ResponseRegister
import com.example.fattrack.data.services.responses.ResponseScanImage
import com.example.fattrack.data.services.responses.ResponseSearchFood
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Url

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


    //Predict / scan image
    @Multipart
    @POST
    suspend fun predict(
        @Url url:String,
        @Header("Authorization") token: String,
        @Part("user_id") userId: RequestBody,
        @Part file: MultipartBody.Part,
    ) : Response<ResponseScanImage>


    //search
    @GET("makanan")
    suspend fun searhFood(
        @Query("nama") nama: String
    ) : Response<ResponseSearchFood>


    //articles
    @GET("articles")
    suspend fun getArticles(
        @Header("Authorization") token: String
    ): Response<ResponseArticle>
}

