package com.example.fattrack.data

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.fattrack.R
import com.example.fattrack.data.database.NotificationDatabase
import com.example.fattrack.data.database.NotificationEntity
import com.example.fattrack.view.notifications.NotificationsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Cek apakah izin POST_NOTIFICATIONS telah diberikan
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Ambil data dari Intent
            val title = intent.getStringExtra("title") ?: "Reminder"
            val message = intent.getStringExtra("message") ?: "It's time to check your calories!"
            val timestamp = System.currentTimeMillis()

            // Simpan notifikasi ke database
            saveNotification(context, title, message, timestamp)

            // Buat PendingIntent untuk membuka NotificationActivity ketika notifikasi diklik
            val notificationIntent = Intent(context, NotificationsActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,  // Request code
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Bangun notifikasi
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            // Tampilkan notifikasi
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(timestamp.toInt(), builder.build())
        } else {
            // Tangani jika izin tidak diberikan
            println("Permission for POST_NOTIFICATIONS is not granted.")
        }
    }

    private fun saveNotification(context: Context, title: String, message: String, timestamp: Long) {
        // Akses database dan simpan data notifikasi
        val notificationDao = NotificationDatabase.getDatabase(context).notificationDao()
        val notification = NotificationEntity(
            id = 0,
            title = title,
            message = message,
            timestamp = timestamp
        )
        CoroutineScope(Dispatchers.IO).launch {
            notificationDao.insertNotification(notification)
        }
    }

    companion object {
        // Channel ID untuk notifikasi (pastikan channel ini dibuat di aplikasi)
        private const val CHANNEL_ID = "default_channel"

        // Fungsi untuk membuat Notification Channel (harus dipanggil pada saat aplikasi dijalankan)
        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, "Reminder Notifications", importance).apply {
                    description = "Channel for reminder notifications"
                }

                // Dapatkan NotificationManager dan buat channel
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}
