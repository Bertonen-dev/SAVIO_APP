package com.example.savio_app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val message: String,
    val token: String,
    @SerialName("userId") val userId: String,
    val nombre: String,
    @SerialName("correo_electronico") val correo_electronico: String,
    val rol: String
)
