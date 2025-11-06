package com.example.savio_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NoteCreateRequest(
    val titulo: String,
    val contenido: String?,
    val idusuario: String
)


