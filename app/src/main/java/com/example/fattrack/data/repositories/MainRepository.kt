package com.example.fattrack.data.repositories

import android.util.Log
import com.example.fattrack.data.pref.AuthPreferences
import com.example.fattrack.data.services.responses.ResponseScanImage
import com.example.fattrack.data.services.retrofit.ApiService
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MainRepository(private val apiService: ApiService, private val authPreferences: AuthPreferences) {
    suspend fun predictImage(image: File): Result<ResponseScanImage> {
        return try {
            // Cek isToken ready
            val idUser = authPreferences.getSession().first().idUser
            val token = authPreferences.getSession().first().token
            if (token.isEmpty() || idUser.isEmpty()) {
                Result.failure<Throwable>(Exception("Token not found"))
            }

            Log.d("PredictResponse", "user id: $idUser, token: $token")

            //converse to multipart
            val imagePart = image.toMultipartBody()

            // get API
            val response = apiService.predict("Bearer $token", imagePart, idUser)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Response body is null"))
                }
            } else {
                // Log error
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("Error because $errorMessage"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private fun File.toMultipartBody(): MultipartBody.Part {
        val requestBody = this.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("photo", this.name, requestBody)
    }


    companion object {
        fun getInstance (apiService: ApiService, authPreferences: AuthPreferences): MainRepository {
            return MainRepository(apiService, authPreferences)
        }
    }
}