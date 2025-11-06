package com.example.savio_app.viewmodel

import com.example.savio_app.data.local.SessionManager
import com.example.savio_app.data.model.Evento
import com.example.savio_app.data.network.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventViewModel(private val sessionManager: SessionManager) {
    private val _eventos = MutableStateFlow<List<Evento>>(emptyList())
    val eventos: StateFlow<List<Evento>> = _eventos

    private val _operationResult = MutableStateFlow<String?>(null)
    val operationResult: StateFlow<String?> = _operationResult

    suspend fun loadEventos() {
        val token = sessionManager.getToken() ?: return
        getEventos(token).onSuccess { eventList ->
            _eventos.value = eventList
        }.onFailure { error ->
            _operationResult.value = "Error al cargar eventos: ${error.message}"
        }
    }

    suspend fun createEvento(titulo: String, descripcion: String, fechaInicio: String, fechaFin: String) {
        val token = sessionManager.getToken() ?: return
        val request = EventoRequest(titulo, descripcion, fechaInicio, fechaFin)

        createEvento(token, request).onSuccess { evento ->
            _operationResult.value = "Evento creado: ${evento.titulo}"
            loadEventos()
        }.onFailure { error ->
            _operationResult.value = "Error al crear evento: ${error.message}"
        }
    }

    suspend fun updateEvento(idevento: Int, titulo: String, descripcion: String, fechaInicio: String, fechaFin: String) {
        val token = sessionManager.getToken() ?: return
        val request = EventoRequest(titulo, descripcion, fechaInicio, fechaFin)

        updateEvento(token, idevento, request).onSuccess { evento ->
            _operationResult.value = "Evento actualizado: ${evento.titulo}"
            loadEventos()
        }.onFailure { error ->
            _operationResult.value = "Error al actualizar evento: ${error.message}"
        }
    }

    suspend fun deleteEvento(idevento: Int) {
        val token = sessionManager.getToken() ?: return
        deleteEvento(token, idevento).onSuccess {
            _operationResult.value = "Evento eliminado correctamente"
            loadEventos()
        }.onFailure { error ->
            _operationResult.value = "Error al eliminar evento: ${error.message}"
        }
    }
}
