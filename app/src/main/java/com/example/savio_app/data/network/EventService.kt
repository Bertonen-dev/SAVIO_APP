package com.example.savio_app.data.network

import com.example.savio_app.data.model.Evento
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import android.util.Base64
import com.example.savio_app.data.network.HttpClientProvider.client as httpClient

private fun getUserId(token: String): String {
    return try {
        val parts = token.split(".")
        if (parts.size != 3) throw Exception("Token inválido")

        val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
        val regex = """"idusuario":"([^"]+)"""".toRegex()
        val matchResult = regex.find(payload)
        matchResult?.groupValues?.get(1) ?: throw Exception("ID de usuario no encontrado en el token")
    } catch (e: Exception) {
        throw Exception("Error al obtener ID de usuario del token: ${e.message}")
    }
}

@Serializable
data class EventoRequest(
    val titulo: String,
    val descripcion: String,
    val fecha_inicio: String,
    val fecha_fin: String
)

@Serializable
data class EventoResponse(
    val message: String,
    val evento: Evento
)

suspend fun getEventos(token: String): Result<List<Evento>> {
    return try {
        val response: HttpResponse = httpClient.get("https://savio-api.onrender.com/usuario/${getUserId(token)}/datos") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                accept(ContentType.Application.Json)
            }
        }

        when (response.status) {
            HttpStatusCode.OK -> {
                val jsonResponse = response.body<JsonObject>()
                val eventosArray = jsonResponse["eventos"]?.jsonArray ?: buildJsonArray { }

                val eventos = eventosArray.mapNotNull { eventoElement ->
                    try {
                        val evento = eventoElement.jsonObject
                        Evento(
                            idevento = evento["idevento"]?.jsonPrimitive?.int ?: 0,
                            titulo = evento["titulo"]?.jsonPrimitive?.content ?: "",
                            descripcion = evento["descripcion"]?.jsonPrimitive?.content ?: "",
                            fecha_inicio = evento["fecha_inicio"]?.jsonPrimitive?.content ?: "",
                            fecha_fin = evento["fecha_fin"]?.jsonPrimitive?.content ?: "",
                            idusuario = evento["idusuario"]?.jsonPrimitive?.content ?: ""
                        )
                    } catch (e: Exception) {
                        println("Error al parsear evento: ${e.message}")
                        null
                    }
                }
                Result.success(eventos)
            }
            else -> {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Error ${response.status.value}: $errorBody"))
            }
        }
    } catch (e: Exception) {
        println("Error en getEventos: ${e.message}")
        e.printStackTrace()
        Result.failure(e)
    }
}

suspend fun createEvento(token: String, request: EventoRequest): Result<Evento> {
    return try {
        val response: HttpResponse = httpClient.post("https://savio-api.onrender.com/eventos") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
            }
            setBody(request)
        }

        when (response.status) {
            HttpStatusCode.Created -> {
                val jsonResponse = response.body<JsonObject>()
                val eventoJson = jsonResponse["evento"]?.jsonObject
                if (eventoJson != null) {
                    val evento = Evento(
                        idevento = eventoJson["idevento"]?.jsonPrimitive?.int ?: 0,
                        titulo = eventoJson["titulo"]?.jsonPrimitive?.content ?: "",
                        descripcion = eventoJson["descripcion"]?.jsonPrimitive?.content ?: "",
                        fecha_inicio = eventoJson["fecha_inicio"]?.jsonPrimitive?.content ?: "",
                        fecha_fin = eventoJson["fecha_fin"]?.jsonPrimitive?.content ?: "",
                        idusuario = eventoJson["idusuario"]?.jsonPrimitive?.content ?: ""
                    )
                    Result.success(evento)
                } else {
                    Result.failure(Exception("Respuesta inválida del servidor"))
                }
            }
            else -> {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Error ${response.status.value}: $errorBody"))
            }
        }
    } catch (e: Exception) {
        println("Error en createEvento: ${e.message}")
        e.printStackTrace()
        Result.failure(e)
    }
}

suspend fun updateEvento(token: String, idevento: Int, request: EventoRequest): Result<Evento> {
    return try {
        val response: HttpResponse = httpClient.put("https://savio-api.onrender.com/eventos/$idevento") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
            }
            setBody(request)
        }

        when (response.status) {
            HttpStatusCode.OK -> {
                val jsonResponse = response.body<JsonObject>()
                val eventoJson = jsonResponse["evento"]?.jsonObject
                if (eventoJson != null) {
                    val evento = Evento(
                        idevento = eventoJson["idevento"]?.jsonPrimitive?.int ?: 0,
                        titulo = eventoJson["titulo"]?.jsonPrimitive?.content ?: "",
                        descripcion = eventoJson["descripcion"]?.jsonPrimitive?.content ?: "",
                        fecha_inicio = eventoJson["fecha_inicio"]?.jsonPrimitive?.content ?: "",
                        fecha_fin = eventoJson["fecha_fin"]?.jsonPrimitive?.content ?: "",
                        idusuario = eventoJson["idusuario"]?.jsonPrimitive?.content ?: ""
                    )
                    Result.success(evento)
                } else {
                    Result.failure(Exception("Respuesta inválida del servidor"))
                }
            }
            else -> {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Error ${response.status.value}: $errorBody"))
            }
        }
    } catch (e: Exception) {
        println("Error en updateEvento: ${e.message}")
        e.printStackTrace()
        Result.failure(e)
    }
}

// Eliminar un evento
suspend fun deleteEvento(token: String, idevento: Int): Result<Unit> {
    return try {
        val response: HttpResponse = httpClient.delete("https://savio-api.onrender.com/eventos/$idevento") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }

        when (response.status) {
            HttpStatusCode.OK -> Result.success(Unit)
            else -> {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Error ${response.status.value}: $errorBody"))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
