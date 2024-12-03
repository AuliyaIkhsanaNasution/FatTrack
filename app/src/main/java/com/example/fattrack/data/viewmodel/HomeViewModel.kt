package com.example.fattrack.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fattrack.data.repositories.MainRepository
import kotlinx.coroutines.launch

class HomeViewModel (private val  mainRepository: MainRepository ) : ViewModel()  {

    private val _userName = MutableLiveData<String?>()
    val userName: LiveData<String?> = _userName

    private val _userPhoto = MutableLiveData<String?>()
    val userPhoto: LiveData<String?> = _userPhoto

    private val _userEmail = MutableLiveData<String?>()
    val userEmail: LiveData<String?> = _userEmail

    private val _totalProtein = MutableLiveData<Double?>()
    val totalProtein: LiveData<Double?> = _totalProtein

    private val _totalKarbohidrat = MutableLiveData<Double?>()
    val totalKarbohidrat: LiveData<Double?> = _totalKarbohidrat

    private val _totalLemak = MutableLiveData<Double?>()
    val totalLemak: LiveData<Double?> = _totalLemak

    private val _date = MutableLiveData<String?>()
    val date: LiveData<String?> = _date

    private val _totalKalori = MutableLiveData<Int?>()
    val totalKalori: LiveData<Int?> = _totalKalori

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _targetKalori = MutableLiveData<Int>(2000)
    val targetKalori: LiveData<Int> = _targetKalori




    fun getUserById() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val result = mainRepository.getUserById()
                result.onSuccess { response ->
                    _userName.value = response.data?.nama
                    _userPhoto.value = response.data?.fotoProfile
                    _userEmail.value = response.data?.email
                }.onFailure { throwable ->
                    _errorMessage.value = throwable.message
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchHomeData() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = mainRepository.homeData()
                result.onSuccess { response ->
                    val homeData = response.data
                    if (homeData != null) {
                        // Parse data to LiveData
                        _totalProtein.value = homeData.totalProtein?.toString()?.toDoubleOrNull()
                        _totalKarbohidrat.value = homeData.totalKarbohidrat?.toString()?.toDoubleOrNull()
                        _totalLemak.value = homeData.totalLemak?.toString()?.toDoubleOrNull()
                        _date.value = homeData.date ?: "Tanggal tidak tersedia"
                        _totalKalori.value = homeData.totalKalori
                    } else {
                        _errorMessage.value = "Data is null"
                    }
                }.onFailure { throwable ->
                    _errorMessage.value = throwable.message
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }


}