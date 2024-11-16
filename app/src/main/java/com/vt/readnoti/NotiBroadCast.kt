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
            val moneySpeaker = MoneySpeaker(context)
            when(packageName){
                "com.VCB" -> {
                    try {
                        moneySpeaker.speakAmount(extractReceivedAmountVCB(content) ?: 0)
                    } catch (e: Exception) {
                        Log.e("MoneySpeaker", "Failed to speak amount", e)
                    }
                }
                "vn.com.techcombank.bb.app" -> {
                    try {
                        moneySpeaker.speakAmount(title.filter { it.isDigit() }.toInt())
                    } catch (e: Exception) {
                        Log.e("MoneySpeaker", "Failed to speak amount", e)
                    }
                }
                "com.mbmobile" -> {
                    try {
                        moneySpeaker.speakAmount(extractReceivedAmountMB(content) ?: 0)
                    } catch (e: Exception) {
                        Log.e("MoneySpeaker", "Failed to speak amount", e)
                    }
                }
                "com.viettinbank.ipay" -> {
                    try {
                        moneySpeaker.speakAmount(extractTransactionAmountViettin(content) ?: 0)
                    } catch (e: Exception) {
                        Log.e("MoneySpeaker", "Failed to speak amount", e)
                    }
                }
                "com.vnpay.Agribank3g" -> {
                    try {
                        moneySpeaker.speakAmount(extractTransactionAmountAgri(content) ?: 0)
                    } catch (e: Exception) {
                        Log.e("MoneySpeaker", "Failed to speak amount", e)
                    }
                }
            }


        }
    }
    private fun extractReceivedAmountMB(notificationContent: String): Int? {
        val regex = """\+(\d{1,3}(,\d{3})*)VND""".toRegex()
        val matchResult = regex.find(notificationContent)
        return matchResult?.groups?.get(1)?.value?.replace(",", "")?.toIntOrNull()
    }
    private fun extractReceivedAmountVCB(notificationContent: String): Int? {
        val regex = """\+(\d{1,3}(,\d{3})*)""".toRegex()
        val matchResult = regex.find(notificationContent)
        return matchResult?.groups?.get(1)?.value?.replace(",", "")?.toIntOrNull()
    }
    private fun extractTransactionAmountAgri(notificationContent: String): Int? {
        val regex = Regex("""Số tiền GD:\s*[+-]?([0-9,.]+)""")
        val matchResult = regex.find(notificationContent)
        return matchResult?.groups?.get(1)?.value?.replace(",", "")?.toIntOrNull()
    }

    fun extractTransactionAmountViettin(notificationContent: String): Int? {
        val regex = """Giao dịch:\s*([+-]?\d{1,3}(,\d{3})*) VND""".toRegex()
        val matchResult = regex.find(notificationContent)
        return matchResult?.groups?.get(1)?.value?.replace(",", "")?.toIntOrNull()
    }


}