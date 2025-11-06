package com.example.savio_app.data.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object OpenAiApi {

    private const val BASE_URL = "https://api.openai.com/v1/chat/completions"
    private const val API_KEY = "ADD_YOUR_OWN_KEY"

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                encodeDefaults = true
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
    }

    suspend fun getChatResponse(userMessage: String): String {
        val request = ChatRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(Message(role = "user", content = userMessage))
        )

        println("🟢 Payload enviado:\n${Json.encodeToString(ChatRequest.serializer(), request)}")

        val response = client.post(BASE_URL) {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $API_KEY")
            }
            setBody(request)
        }

        val rawResponse = response.bodyAsText()
        println("🔵 Respuesta cruda:\n$rawResponse")

        return try {
            val json = Json { ignoreUnknownKeys = true }
            val chatResponse = json.decodeFromString(ChatResponse.serializer(), rawResponse)
            chatResponse.choices.firstOrNull()?.message?.content ?: "No se recibió respuesta"
        } catch (e: Exception) {
            try {
                val errorObject = Json.parseToJsonElement(rawResponse)
                val errorMessage = errorObject.jsonObject["error"]?.jsonObject?.get("message")?.toString()
                "Error del servidor: $errorMessage"
            } catch (inner: Exception) {
                "Error al procesar la respuesta: ${e.message}\nRespuesta cruda: $rawResponse"
            }
        }
    }
}
