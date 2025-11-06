package com.example.savio_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ShoppingListListResponse(
    val listas: List<ShoppingListData>
)
