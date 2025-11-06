package com.example.savio_app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val nombre: String,
    val apellidos: String,
    @SerialName("correo_electronico") val correo_electronico: String,
    val contrasena: String,
    @SerialName("fecha_nacimiento") val fecha_nacimiento: String,
    val telefono: String
)
