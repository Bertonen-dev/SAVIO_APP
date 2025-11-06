package com.example.savio_app.data.repository

import android.content.Context
import com.example.savio_app.data.model.Evento
import com.example.savio_app.data.network.EventoRequest
import com.example.savio_app.data.network.createEvento
import com.example.savio_app.data.network.deleteEvento
import com.example.savio_app.data.network.getEventos
import com.example.savio_app.data.network.updateEvento
import com.example.savio_app.util.EventNotificationScheduler

class EventRepository(private val context: Context) {
    private val notificationScheduler = EventNotificationScheduler(context)

    suspend fun getEventos(token: String): Result<List<Evento>> {
        return getEventos(token).also { result ->
            result.getOrNull()?.forEach { evento ->
                notificationScheduler.scheduleNotification(evento)
            }
        }
    }

    suspend fun createEvento(token: String, request: EventoRequest): Result<Evento> {
        return createEvento(token, request).also { result ->
            result.getOrNull()?.let { evento ->
                notificationScheduler.scheduleNotification(evento)
            }
        }
    }

    suspend fun updateEvento(token: String, idevento: Int, request: EventoRequest): Result<Evento> {
        return updateEvento(token, idevento, request).also { result ->
            result.getOrNull()?.let { evento ->
                notificationScheduler.scheduleNotification(evento)
            }
        }
    }

    suspend fun deleteEvento(token: String, idevento: Int): Result<Unit> {
        return deleteEvento(token, idevento)
    }
}
