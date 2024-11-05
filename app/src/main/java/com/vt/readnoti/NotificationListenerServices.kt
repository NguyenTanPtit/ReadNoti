package com.vt.readnoti

import android.app.Notification
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListenerServices: NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        val notification = sbn.notification
        val extras = notification.extras
        val packageName = sbn.packageName
        val title = extras.getString(Notification.EXTRA_TITLE)
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)
        val bigText = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)
        val subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT)
        val notificationContent = "Title: $title\nText: $text\nSub Text: $subText"

        var content = text
        if(content.isNullOrEmpty()){
            content = if(bigText.isNullOrEmpty()) subText else bigText
        }
        val intent = Intent("com.vt.readnoti.NOTIFICATION_LISTENER_EXAMPLE")
        intent.putExtra("title", title)
        intent.putExtra("content", content)
        intent.putExtra("packageName", packageName)
        Log.d("NotificationListenerService", notificationContent)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?, rankingMap: RankingMap?) {
        super.onNotificationRemoved(sbn, rankingMap)
        Log.d("NotificationListenerService", "Notification Removed")
    }
}