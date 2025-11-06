package com.example.savio_app.data.network

import com.example.savio_app.data.model.UserDataResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun getUserData(token: String, userId: String): Result<UserDataResponse> {
    return try {
        val response: HttpResponse = httpClient.get("https://savio-api.onrender.com/usuario/$userId/datos") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }

        when (response.status) {
            HttpStatusCode.OK -> {
                val userData = response.body<UserDataResponse>()
                Result.success(userData)
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
