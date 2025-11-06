package com.example.savio_app.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.savio_app.data.model.Evento
import java.text.SimpleDateFormat
import java.util.*

class EventNotificationScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    fun scheduleNotification(evento: Evento) {
        try {
            val startDate = dateFormat.parse(evento.fecha_inicio)
            if (startDate != null && startDate.time > System.currentTimeMillis()) {
                val intent = Intent(context, EventNotificationReceiver::class.java).apply {
                    putExtra("eventoTitulo", evento.titulo)
                    putExtra("eventoDescripcion", evento.descripcion)
                }

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    evento.idevento,  // Cambiado de id a idevento
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val notificationTime = startDate.time - (15 * 60 * 1000)

                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    notificationTime,
                    pendingIntent
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancelNotification(evento: Evento) {
        val intent = Intent(context, EventNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            evento.idevento,  // Cambiado de id a idevento
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}
