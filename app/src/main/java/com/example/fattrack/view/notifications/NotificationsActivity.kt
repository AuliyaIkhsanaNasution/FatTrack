package com.example.fattrack.view.notifications

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fattrack.data.ViewModelFactory
import com.example.fattrack.data.adapter.NotificationAdapter
import com.example.fattrack.data.viewmodel.NotificationViewModel
import com.example.fattrack.databinding.ActivityNotificationsBinding
import kotlinx.coroutines.launch

class NotificationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationsBinding
    private val notificationViewModel: NotificationViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // init ViewBinding
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // back button clicked
        binding.backButton.setOnClickListener {
            finish()
        }

        setupRecyclerView()
        loadNotifications()
    }

    private fun setupRecyclerView() {
        binding.rvNotification.layoutManager = LinearLayoutManager(this)
    }

    private fun loadNotifications() {
        notificationViewModel.viewModelScope.launch {
            val notifications = notificationViewModel.getNotifications()
            binding.rvNotification.adapter = NotificationAdapter(notifications)
        }
    }
    }
