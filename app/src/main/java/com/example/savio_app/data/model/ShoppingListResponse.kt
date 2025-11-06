package com.example.savio_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ShoppingListResponse(
    val message: String,
    val lista: ShoppingListData
)

@Serializable
data class ShoppingListData(
    val idlista: Int,
    val titulo: String,
    val fecha_creacion: String,
    val idusuario: String
)
