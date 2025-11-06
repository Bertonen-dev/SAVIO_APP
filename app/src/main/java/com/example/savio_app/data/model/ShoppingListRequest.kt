package com.example.savio_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ShoppingListRequest(
    val titulo: String,
    val idusuario: String
)
