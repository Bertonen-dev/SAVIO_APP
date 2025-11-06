package com.example.savio_app.weather

import com.example.savio_app.data.network.HttpClientProvider
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

object WeatherService {

    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/weather"
    private const val API_KEY = "65dadf2f5cac657af0fc87bd84fab8e7"

    suspend fun getWeatherByCity(city: String): WeatherResponse? {
        return try {
            HttpClientProvider.client.get(BASE_URL) {
                parameter("q", city)
                parameter("appid", API_KEY)
                parameter("units", "metric")
                parameter("lang", "es")
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

