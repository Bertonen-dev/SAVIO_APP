package com.example.savio_app.data.model

import com.google.gson.annotations.SerializedName

data class UserDataResponse(
    @SerializedName("configuracion")
    val configuracion: List<Configuracion> = emptyList(),

    @SerializedName("eventos")
    val eventos: List<Evento> = emptyList(),

    @SerializedName("notas")
    val notas: List<Note> = emptyList(),

    @SerializedName("recordatorios")
    val recordatorios: List<Recordatorio> = emptyList(),

    @SerializedName("productos_lista")
    val productos_lista: List<ProductoLista> = emptyList(),

    @SerializedName("listas_compras")
    val listas_compras: List<ListaCompra> = emptyList()
)

data class Configuracion(
    @SerializedName("idconfiguracion") val idconfiguracion: Int,
    @SerializedName("tema") val tema: String?,
    @SerializedName("idioma") val idioma: String?,
    @SerializedName("idusuario") val idusuario: String
)

data class Recordatorio(
    @SerializedName("idrecordatorio") val idrecordatorio: Int,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("fecha_recordatorio") val fecha_recordatorio: String,
    @SerializedName("frecuencia") val frecuencia: String?,
    @SerializedName("idusuario") val idusuario: String
)

data class ProductoLista(
    @SerializedName("idproducto") val idproducto: Int,
    @SerializedName("nombre_producto") val nombre_producto: String,
    @SerializedName("cantidad") val cantidad: String?,
    @SerializedName("idlista") val idlista: Int,
    @SerializedName("comprado") val comprado: Boolean,
    @SerializedName("idusuario") val idusuario: String
)

data class ListaCompra(
    @SerializedName("idlista") val idlista: Int,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("fecha_creacion") val fecha_creacion: String,
    @SerializedName("idusuario") val idusuario: String
)
