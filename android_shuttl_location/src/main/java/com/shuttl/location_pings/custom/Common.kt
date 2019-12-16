package com.shuttl.location_pings.custom

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.shuttl.locations_sync.R

fun notification(context: Context, value: String): Notification? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel("loc",
                "Locations",
                NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(context, "loc")
                .setContentTitle("Syncing Locations")
                .setContentText(value)
                .setSmallIcon(R.drawable.ic_loc)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        return builder.build()
    } else {
        val builder = NotificationCompat.Builder(context, "loc")
                .setContentTitle("Syncing Locations")
                .setContentText(value)
                .setSmallIcon(R.drawable.ic_loc)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        return builder.build()
    }
}
