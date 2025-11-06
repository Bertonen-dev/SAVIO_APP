package com.example.savio_app.data.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun List<ShoppingListData>.toUiModel(): List<ShoppingList> = this.map {
    val creationDate = try {
        LocalDateTime.parse(it.fecha_creacion, DateTimeFormatter.ISO_DATE_TIME)
    } catch (e: Exception) {
        LocalDateTime.now()
    }
    ShoppingList(
        id = it.idlista,
        title = it.titulo,
        creationDate = creationDate
    )
}
