package com.example.savio_app.data.network

import com.example.savio_app.data.model.LoginRequest
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import com.example.savio_app.data.network.httpClient
import com.example.savio_app.data.model.LoginResponse

@Serializable
data class LoginResponse(
    val message: String,
    val token: String,
    val userId: String,
    val nombre: String,
    val correo_electronico: String,
    val rol: String
)

suspend fun loginUser(email: String, password: String): Result<LoginResponse> {
    return try {
        val response: HttpResponse = httpClient.post("https://savio-api.onrender.com/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }

        when (response.status) {
            HttpStatusCode.OK -> {
                val loginResponse: LoginResponse = response.body()
                Result.success(loginResponse)
            }
            HttpStatusCode.Unauthorized -> {
                Result.failure(Exception("Credenciales incorrectas"))
            }
            else -> {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Error ${response.status.value}: $errorBody"))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

