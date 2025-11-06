package com.example.savio_app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("correo_electronico") val email: String,
    @SerialName("contrasena") val password: String
)

