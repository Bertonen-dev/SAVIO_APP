package com.example.savio_app.weather

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind
)

@Serializable
data class Main(
    val temp: Double,
    val humidity: Int
)

@Serializable
data class Weather(
    val description: String
)

@Serializable
data class Wind(
    val speed: Double
)
