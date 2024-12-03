package com.example.fattrack.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fattrack.data.repositories.MainRepository
import com.example.fattrack.data.services.responses.ResponseScanImage
import com.example.fattrack.data.services.responses.ResponseSearchFood
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

class PredictViewModel(private val mainRepository: MainRepository) : ViewModel() {
    //live data
    private val _predictResponse = MutableLiveData<ResponseScanImage>()
    val predictResponse: LiveData<ResponseScanImage> = _predictResponse

    private val _searchResponse = MutableLiveData<ResponseSearchFood?>()
    val searchResponse: LiveData<ResponseSearchFood?> = _searchResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    //predict image
    fun predictImage(image: File) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                //send
                val response = mainRepository.predictImage(image)

                //handle response
                response.onSuccess {
                    _predictResponse.value = it
                    Log.d("PredictResponse", "Success: $it")
                } .onFailure {
                    _errorMessage.value = it.message.toString()
                    Log.d("PredictResponse", it.message.toString())
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
                Log.d("PredictResponse", e.message.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }



    // Search by name
    fun searchFood(nama: String) {
        _isLoading.value = true
        _searchResponse.value = null

        // validate
        if (nama.isBlank()) {
            _errorMessage.value = "Please enter a food name."
            _isLoading.value = false
            return
        }

        viewModelScope.launch {
            try {
                val filteredName = nama.trim().lowercase(Locale.ROOT)
                val response = mainRepository.searchFood(filteredName)

                response.onSuccess{
                    _searchResponse.value = it
                    Log.d("SearchFood", "Success: $it")
                }.onFailure {
                    _errorMessage.value = it.message ?: "An error occurred while searching for food."
                    Log.e("SearchFood", "Failure: ${it.message}")
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "An error occurred while searching for food."
                Log.e("SearchFood", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


}