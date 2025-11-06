package com.example.savio_app.data.network

import com.example.savio_app.data.model.RegisterRequest
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val message: String
)

suspend fun registerUser(registerRequest: RegisterRequest): Result<RegisterResponse> {
    return try {
        val response: HttpResponse = httpClient.post("https://savio-api.onrender.com/registro") {
            contentType(ContentType.Application.Json)
            setBody(registerRequest)
        }

        when (response.status) {
            HttpStatusCode.Created, HttpStatusCode.OK -> {
                val responseBody = response.body<RegisterResponse>()
                Result.success(responseBody)
            }
            HttpStatusCode.Conflict -> {
                Result.failure(Exception("Ya existe un usuario con ese correo"))
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

@Serializable
data class RecoveryRequest(val correo_electronico: String)

@Serializable
data class RecoveryResponse(val token: String)

suspend fun requestPasswordRecovery(email: String): Result<RecoveryResponse> {
    return try {
        val response: HttpResponse = httpClient.post("https://savio-api.onrender.com/recuperar-contrasena") {
            contentType(ContentType.Application.Json)
            setBody(RecoveryRequest(email))
        }

        if (response.status == HttpStatusCode.OK) {
            val body = response.body<RecoveryResponse>()
            Result.success(body)
        } else {
            val error = response.bodyAsText()
            Result.failure(Exception("Error ${response.status.value}: $error"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

