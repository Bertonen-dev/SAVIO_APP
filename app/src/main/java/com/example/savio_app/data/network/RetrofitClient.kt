package com.example.savio_app.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor // Para ver los logs de red en Logcat
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory // Para convertir JSON a objetos Kotlin

object RetrofitClient {
    private const val BASE_URL = "https://savio-api.onrender.com"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val reminderApiService: ReminderApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ReminderApiService::class.java)
    }
}