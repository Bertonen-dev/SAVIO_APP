package com.example.savio_app.data.network

import android.util.Log
import com.example.savio_app.data.model.RegisterRequest
import com.example.savio_app.data.model.RegisterResponse
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

private val client = HttpClient(Android) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                Log.d("HTTP Client", message)
            }
        }
    }
    expectSuccess = false
}

private const val BASE_URL = "https://savio-api.onrender.com"

@kotlinx.serialization.Serializable
data class ErrorResponse(
    val error: String
)

suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
    return try {
        Log.d("RegisterAPI", "Iniciando registro de usuario")
        Log.d("RegisterAPI", "Request completo: $request")
        Log.d("RegisterAPI", "URL: $BASE_URL/register")

        val response = client.post("$BASE_URL/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
            Log.d("RegisterAPI", "Headers de la petición: ${this.headers}")
        }

        val responseBody = response.body<String>()
        Log.d("RegisterAPI", "Código de respuesta: ${response.status}")
        Log.d("RegisterAPI", "Cuerpo de la respuesta: $responseBody")

        if (response.status.isSuccess()) {
            Result.success(Json.decodeFromString(responseBody))
        } else {
            try {
                val errorResponse = Json.decodeFromString<ErrorResponse>(responseBody)
                Log.e("RegisterAPI", "Error en registro: ${errorResponse.error}")
                Result.failure(Exception(errorResponse.error))
            } catch (e: Exception) {
                Log.e("RegisterAPI", "Error al decodificar respuesta de error: $responseBody", e)
                Result.failure(Exception("Error inesperado: $responseBody"))
            }
        }
    } catch (e: Exception) {
        Log.e("RegisterAPI", "Excepción durante el registro", e)
        Result.failure(Exception("Error de conexión: ${e.message}"))
    }
}

