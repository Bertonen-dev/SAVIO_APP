package com.example.savio_app.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Evento(
    @SerializedName("idevento")
    val idevento: Int,

    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("fecha_inicio")
    val fecha_inicio: String,

    @SerializedName("fecha_fin")
    val fecha_fin: String,

    @SerializedName("idusuario")
    val idusuario: String
)
