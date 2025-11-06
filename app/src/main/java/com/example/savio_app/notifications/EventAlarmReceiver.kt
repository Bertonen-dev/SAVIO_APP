package com.example.savio_app.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class EventAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val eventId = intent.getIntExtra("eventId", 0)
        val title = intent.getStringExtra("title") ?: "Evento"
        val description = intent.getStringExtra("description") ?: ""

        val notificationService = NotificationService(context)
        notificationService.showNotification(eventId, title, description)
    }
}
