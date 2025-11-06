package com.example.savio_app.data.repository

import com.example.savio_app.data.model.Reminder
import com.example.savio_app.data.model.ReminderCreateRequest
import com.example.savio_app.data.model.ReminderUpdateRequest
import com.example.savio_app.data.model.ReminderResponse
import com.example.savio_app.data.network.ReminderApiService
import com.example.savio_app.data.network.RetrofitClient

class ReminderRepository(private val apiService: ReminderApiService) {

    constructor() : this(RetrofitClient.reminderApiService)

    suspend fun getReminders(token: String): Result<List<Reminder>> {
        return try {
            val response = apiService.getReminders("Bearer $token")
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception("Error ${response.code()} al obtener recordatorios: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createReminder(token: String, request: ReminderCreateRequest): Result<Reminder> {
        return try {
            val response = apiService.createReminder("Bearer $token", request)
            if (response.isSuccessful && response.body() != null) {
                response.body()?.let { reminderResponse ->
                    Result.success(reminderResponse.reminder)
                } ?: Result.failure(Exception("Respuesta de creación de recordatorio incompleta o nula."))
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception("Error ${response.code()} al crear recordatorio: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateReminder(token: String, reminderId: Int, request: ReminderUpdateRequest): Result<Reminder> {
        return try {
            val response = apiService.updateReminder("Bearer $token", reminderId, request)
            if (response.isSuccessful) {
                response.body()?.let { reminderResponse ->
                    Result.success(reminderResponse.reminder)
                } ?: Result.failure(Exception("Respuesta de actualización de recordatorio incompleta o nula."))
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception("Error ${response.code()} al actualizar recordatorio: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReminder(token: String, reminderId: Int): Result<Unit> {
        return try {
            val response = apiService.deleteReminder("Bearer $token", reminderId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception("Error ${response.code()} al eliminar recordatorio: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}