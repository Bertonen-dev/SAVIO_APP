package com.example.savio_app.data.model

import com.google.gson.annotations.SerializedName

data class Note(
    @SerializedName("idnota")
    val id: Int,

    @SerializedName("titulo")
    val title: String,

    @SerializedName("contenido")
    val content: String?,

    @SerializedName("idusuario")
    val userId: String,

    val createdAt: String? = null,
    val updatedAt: String? = null

)

