package com.example.fattrack.data

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.BroadcastReceiver
import android.content.pm.PackageManager
import com.example.fattrack.R
import com.example.fattrack.view.notifications.NotificationsActivity

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Periksa apakah izin POST_NOTIFICATIONS telah diberikan
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Ambil data dari Intent
            val title = intent.getStringExtra("title") ?: "Reminder"
            val message = intent.getStringExtra("message") ?: "It's time to check your calories!"

            // Bangun intent untuk membuka NotificationActivity ketika notifikasi diklik
            val notificationIntent = Intent(context, NotificationsActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,  // Request code
                notificationIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                else PendingIntent.FLAG_UPDATE_CURRENT
            )

            // Bangun notifikasi
            val builder = NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)  // PendingIntent untuk membuka activity
                .setAutoCancel(true)  // Notifikasi akan hilang setelah diklik

            // Tampilkan notifikasi
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
        } else {
            // Log atau tangani jika izin tidak diberikan
            println("Permission for POST_NOTIFICATIONS is not granted.")
        }
    }
}
