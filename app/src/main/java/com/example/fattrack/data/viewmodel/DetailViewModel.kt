package com.example.fattrack.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fattrack.data.repositories.ArticleRepository
import com.example.fattrack.data.services.responses.DetailArticleData
import kotlinx.coroutines.launch

class DetailViewModel(private val articleRepository: ArticleRepository) : ViewModel() {

    // LiveData untuk menampung detail artikel
    private val _articleDetail = MutableLiveData<DetailArticleData?>()
    val articleDetail: LiveData<DetailArticleData?> = _articleDetail

    // LiveData untuk menampung status loading
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData untuk menampung pesan error
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Fungsi untuk mengambil detail artikel berdasarkan ID
    fun fetchArticleDetail(articleId: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            val tokenError = "Token not found"
            try {
                // Ambil hasil dari repository
                val result = articleRepository.getDetailArticle(articleId)
                result.onSuccess { response ->
                    _articleDetail.value = response.data
                }.onFailure { throwable ->
                    _errorMessage.value = if (throwable.message == tokenError) {
                        "Autentikasi gagal, silakan login ulang"
                    } else {
                        throwable.message ?: "Terjadi kesalahan tak terduga"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Terjadi kesalahan tak terduga"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
