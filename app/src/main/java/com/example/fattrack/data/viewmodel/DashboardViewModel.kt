package com.example.fattrack.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fattrack.data.repositories.DashboardRepository
import com.example.fattrack.data.services.responses.ResponseDashboardMonth
import com.example.fattrack.data.services.responses.ResponseDashboardWeek
import com.example.fattrack.data.services.responses.ResponseHistory
import kotlinx.coroutines.launch

class DashboardViewModel(private val dashboardRepository: DashboardRepository): ViewModel() {
    //live data
    private val _dashboardWeekResponse = MutableLiveData<ResponseDashboardWeek>()
    val dashboardWeekResponse: LiveData<ResponseDashboardWeek> = _dashboardWeekResponse

    private val _dashboardMonthResponse = MutableLiveData<ResponseDashboardMonth>()
    val dashboardMonthResponse: LiveData<ResponseDashboardMonth> = _dashboardMonthResponse

    private val _historyResponse = MutableLiveData<ResponseHistory>()
    val historyResponse: LiveData<ResponseHistory> = _historyResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    //week
    fun dashboardWeek() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                //send
                val response = dashboardRepository.getDashboardWeek()

                //handle response
                response.onSuccess {
                    _dashboardWeekResponse.value = it
                }.onFailure {
                    _errorMessage.value = it.message.toString()
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
            } finally {
                _isLoading.value = false
            }
        }
    }


    //Month
    fun dashboardMonth() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                //send
                val response = dashboardRepository.getDashboardMonth()

                //handle response
                response.onSuccess {
                    _dashboardMonthResponse.value = it
                }.onFailure {
                    _errorMessage.value = it.message.toString()
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
            } finally {
                _isLoading.value = false
            }
        }
    }


    //history
    fun getHistory() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                //send
                val response = dashboardRepository.getHistory()

                //handle response
                response.onSuccess {
                    _historyResponse.value = it
                }.onFailure {
                    _errorMessage.value = it.message.toString()
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
