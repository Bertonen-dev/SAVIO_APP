package com.example.savio_app.data.network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import com.example.savio_app.data.network.HttpClientProvider.client as httpClient

suspend fun deleteShoppingList(token: String, id: Int): Result<Unit> {
    return try {
        val response: HttpResponse = httpClient.delete("https://savio-api.onrender.com/listas_compras/$id") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                accept(ContentType.Application.Json)
            }
        }

        when (response.status) {
            HttpStatusCode.NoContent, HttpStatusCode.OK -> Result.success(Unit)
            else -> {
                val error = response.bodyAsText()
                Result.failure(Exception("Error ${response.status.value}: $error"))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
