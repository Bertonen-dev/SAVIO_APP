package com.example.savio_app.data.network

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>,
    val temperature: Double = 0.7
)

@Serializable
data class Message(
    val role: String,
    val content: String,
    val refusal: String? = null,
    val annotations: List<String> = emptyList()
)

@Serializable
data class ChatResponse(
    val choices: List<Choice>
)

@Serializable
data class Choice(
    val index: Int? = null,
    val message: Message,
    val finish_reason: String? = null
)
