package com.example.fattrack.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.fattrack.data.database.NotificationEntity
import com.example.fattrack.data.datastore.NotificationScheduler
import com.example.fattrack.data.repositories.NotificationRepository
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: NotificationRepository, private val notificationScheduler: NotificationScheduler) : ViewModel() {

    val notificationToggleState = repository.notificationToggleFlow.asLiveData()

    fun setNotificationToggle(enabled: Boolean) {
        viewModelScope.launch {
            repository.setNotificationToggle(enabled)

            if (enabled) {
                scheduleNotifications()
            } else {
                cancelScheduledNotifications()
            }
        }
    }

    fun saveNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            repository.saveNotification(notification)
        }
    }

    suspend fun getNotifications(): List<NotificationEntity> {
        return repository.getNotifications()
    }

    private fun scheduleNotifications() {
        notificationScheduler.scheduleNotification(8, 0)  // 8 AM
        notificationScheduler.scheduleNotification(12, 0) // 12 PM
        notificationScheduler.scheduleNotification(19, 0) // 7 PM
    }

    private fun cancelScheduledNotifications() {
        notificationScheduler.cancelNotifications()
    }
}
