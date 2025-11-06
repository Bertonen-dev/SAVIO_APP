package com.example.savio_app.data.model

import java.time.LocalDateTime

data class ShoppingList(
    val id: Int,
    val title: String,
    val creationDate: LocalDateTime
)
