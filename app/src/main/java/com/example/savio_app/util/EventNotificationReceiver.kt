package com.example.savio_app.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class EventNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val titulo = intent.getStringExtra("eventoTitulo") ?: "Evento próximo"
        val descripcion = intent.getStringExtra("eventoDescripcion") ?: "Tienes un evento programado"

        val notificationHelper = NotificationHelper(context)
        notificationHelper.showEventoNotification(titulo, descripcion)
    }
}
