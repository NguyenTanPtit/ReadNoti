package com.vt.readnoti

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.vt.readnoti.databinding.ActivityMainBinding

class NotiBroadCast(private val binding : ActivityMainBinding) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("title")
        val content = intent?.getStringExtra("content")
        val packageName = intent?.getStringExtra("packageName")
        val notificationContent = "Title: $title\nContent: $content\nPackage Name: $packageName"
        Log.d("NotificationListenerService", notificationContent)
        binding.title.text = title
        binding.content.text = content
        if(context!=null && packageName!="com.vt.readnoti" && title!=null && content!=null){
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel("ReadNoti", "ReadNoti", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
            val notification = android.app.Notification.Builder(context, "ReadNoti")
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setChannelId("ReadNoti")
                .build()

            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE)
            notification.contentIntent = pendingIntent
            notificationManager.notify(1, notification)

        }
    }
}