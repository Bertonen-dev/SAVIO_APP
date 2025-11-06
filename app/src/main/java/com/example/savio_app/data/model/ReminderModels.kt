package com.example.savio_app.data.model

import com.google.gson.annotations.SerializedName

data class Reminder(
    @SerializedName("idrecordatorio") val id: Int,
    @SerializedName("titulo") val title: String,
    @SerializedName("descripcion") val description: String?,
    @SerializedName("fecha_recordatorio") val reminderDateTime: String,
    @SerializedName("frecuencia") val frequency: String?,
    @SerializedName("idusuario") val userId: String
)

data class ReminderCreateRequest(
    @SerializedName("titulo") val title: String,
    @SerializedName("descripcion") val description: String?,
    @SerializedName("fecha_recordatorio") val reminderDateTime: String,
    @SerializedName("frecuencia") val frequency: String?
)
data class ReminderUpdateRequest(
    @SerializedName("titulo") val title: String?,
    @SerializedName("descripcion") val description: String?,
    @SerializedName("fecha_recordatorio") val reminderDateTime: String?,
    @SerializedName("frecuencia") val frequency: String?
)

data class ReminderResponse(
    @SerializedName("message") val message: String,
    @SerializedName("recordatorio") val reminder: Reminder
)