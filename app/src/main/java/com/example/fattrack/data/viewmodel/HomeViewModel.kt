package com.example.fattrack.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fattrack.data.repositories.MainRepository
import kotlinx.coroutines.launch

class HomeViewModel (private val mainRepository: MainRepository) : ViewModel()  {

    // LiveData untuk nama dan foto profil
    private val _userName = MutableLiveData<String?>()
    val userName: LiveData<String?> = _userName

    private val _userPhoto = MutableLiveData<String?>()
    val userPhoto: LiveData<String?> = _userPhoto

    // LiveData untuk status loading
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData untuk pesan error
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Fungsi untuk mengambil data user berdasarkan ID
    fun getUserById() {
        _isLoading.value = true // Mulai loading
        _errorMessage.value = null // Reset error

        viewModelScope.launch {
            try {
                val result = mainRepository.getUserById()
                result.onSuccess { response ->
                    // Update nama dan foto profil ke LiveData
                    _userName.value = response.data?.nama
                    _userPhoto.value = response.data?.fotoProfile
                }.onFailure { throwable ->
                    _errorMessage.value = throwable.message // Set error jika gagal
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message // Tangani error yang tak terduga
            } finally {
                _isLoading.value = false // Selesai loading
            }
        }
    }
}