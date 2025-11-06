package com.example.savio_app.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session")

object SessionKeys {
    val TOKEN = stringPreferencesKey("token")
    val EMAIL = stringPreferencesKey("email")
    val ROL = stringPreferencesKey("rol")
    val NOMBRE = stringPreferencesKey("nombre")
    val IDUSUARIO = stringPreferencesKey("idusuario")
}

// Clase que representa los datos completos de sesión
data class SessionData(
    val token: String?,
    val email: String?,
    val rol: String?,
    val nombre: String?,
    val idusuario: String?
)


class SessionManager(private val context: Context) {

    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[SessionKeys.TOKEN] }
    val emailFlow: Flow<String?> = context.dataStore.data.map { it[SessionKeys.EMAIL] }
    val rolFlow: Flow<String?> = context.dataStore.data.map { it[SessionKeys.ROL] }
    val nombreFlow: Flow<String?> = context.dataStore.data.map { it[SessionKeys.NOMBRE] }

    suspend fun saveSession(token: String, email: String, rol: String, nombre: String, idusuario: String) {
        context.dataStore.edit { prefs ->
            prefs[SessionKeys.TOKEN] = token
            prefs[SessionKeys.EMAIL] = email
            prefs[SessionKeys.ROL] = rol
            prefs[SessionKeys.NOMBRE] = nombre
            prefs[SessionKeys.IDUSUARIO] = idusuario
        }
    }


    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun getSession(): SessionData {
        val prefs = context.dataStore.data.first()
        return SessionData(
            token = prefs[SessionKeys.TOKEN],
            email = prefs[SessionKeys.EMAIL],
            rol = prefs[SessionKeys.ROL],
            nombre = prefs[SessionKeys.NOMBRE],
            idusuario = prefs[SessionKeys.IDUSUARIO]
        )
    }

    suspend fun getIdUsuario(): String? {
        return getSession().idusuario
    }

    suspend fun getToken(): String? {
        return getSession().token
    }
}
