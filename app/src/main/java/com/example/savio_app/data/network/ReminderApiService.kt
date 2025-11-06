package com.example.savio_app.data.network

import com.example.savio_app.data.model.Reminder
import com.example.savio_app.data.model.ReminderCreateRequest
import com.example.savio_app.data.model.ReminderResponse
import com.example.savio_app.data.model.ReminderUpdateRequest
import retrofit2.Response // Importa esta clase para manejar las respuestas HTTP
import retrofit2.http.* // Importa todas las anotaciones de Retrofit

interface ReminderApiService {

    @GET("recordatorios")
    suspend fun getReminders(@Header("Authorization") token: String): Response<List<Reminder>>

    @POST("recordatorios")
    suspend fun createReminder(
        @Header("Authorization") token: String,
        @Body request: ReminderCreateRequest
    ): Response<ReminderResponse>

    @PUT("recordatorios/{idrecordatorio}")
    suspend fun updateReminder(
        @Header("Authorization") token: String,
        @Path("idrecordatorio") reminderId: Int,
        @Body request: ReminderUpdateRequest
    ): Response<ReminderResponse>

    @DELETE("recordatorios/{idrecordatorio}")
    suspend fun deleteReminder(
        @Header("Authorization") token: String,
        @Path("idrecordatorio") reminderId: Int
    ): Response<Unit>
}