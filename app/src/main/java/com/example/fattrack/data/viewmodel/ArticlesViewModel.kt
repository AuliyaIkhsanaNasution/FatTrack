package com.example.fattrack.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fattrack.data.repositories.ArticleRepository
import com.example.fattrack.data.services.responses.DataItem
import kotlinx.coroutines.launch

class ArticlesViewModel(private val articleRepository: ArticleRepository) : ViewModel() {

    private val _articles = MutableLiveData<List<DataItem>>()
    val articles: LiveData<List<DataItem>> = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun fetchArticles() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val result = articleRepository.getListArticles()
                result.onSuccess { response ->
                    _articles.value = response.data?.filterNotNull()
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

