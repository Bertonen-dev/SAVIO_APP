package com.example.savio_app.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savio_app.data.model.Reminder
import com.example.savio_app.data.model.ReminderCreateRequest
import com.example.savio_app.data.model.ReminderUpdateRequest
import com.example.savio_app.data.repository.ReminderRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Clase de datos que representa un recordatorio en la interfaz de usuario.
 * Esta clase actúa como un modelo de presentación para la vista de recordatorios.
 *
 * @property id Identificador único del recordatorio
 * @property title Título del recordatorio
 * @property description Descripción opcional del recordatorio
 * @property reminderDateTime Fecha y hora del recordatorio en formato ISO 8601
 * @property frequency Frecuencia del recordatorio (diario, semanal, etc.)
 * @property userId Identificador del usuario al que pertenece el recordatorio
 * @property isDone Estado del recordatorio (completado o no)
 */
/**
 * ViewModel que maneja la lógica de negocio y el estado de la pantalla de recordatorios (ToDo).
 * Gestiona las operaciones CRUD de recordatorios y mantiene el estado de la UI.
 */
    /**
     * Establece el token de autenticación y carga los recordatorios asociados a este token.
     *
     * @param token El token de autenticación del usuario
     */
    /**
     * Carga los recordatorios del usuario autenticado.
     * Realiza una llamada a la capa de repositorio para obtener los recordatorios.
     */
    /**
     * Crea un nuevo recordatorio con los detalles proporcionados.
     *
     * @param title Título del recordatorio
     * @param description Descripción opcional del recordatorio
     * @param year Año de la fecha y hora del recordatorio
     * @param month Mes de la fecha y hora del recordatorio (0-11)
     * @param day Día del mes de la fecha y hora del recordatorio
     * @param hour Hora de la fecha y hora del recordatorio
     * @param minute Minuto de la fecha y hora del recordatorio
     * @param frequency Frecuencia del recordatorio (diario, semanal, etc.)
     */
    /**
     * Actualiza un recordatorio existente con los nuevos detalles proporcionados.
     *
     * @param uiReminder El recordatorio actualizado
     */
    /**
     * Elimina un recordatorio por su identificador.
     *
     * @param reminderId El identificador del recordatorio a eliminar
     */
    /**
     * Cambia el estado de completado de un recordatorio.
     *
     * @param reminderId El identificador del recordatorio
     * @param isChecked El nuevo estado de completado
     */
    /**
     * Formatea la cadena de fecha y hora de un recordatorio para mostrar solo la hora.
     *
     * @param dateTimeString La cadena de fecha y hora en formato ISO 8601
     * @return La hora formateada como cadena, o "Hora inválida" si el formato es incorrecto
     */
data class UiReminder(
    val id: Int,
    val title: String,
    val description: String?,
    val reminderDateTime: String,
    val frequency: String?,
    val userId: String,
    var isDone: Boolean = false
)

class ToDoViewModel(
    private val repository: ReminderRepository = ReminderRepository()
) : ViewModel() {

    val reminders = mutableStateListOf<UiReminder>()
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    private var authToken: String = ""

    fun setAuthToken(token: String) {
        this.authToken = token
        loadReminders()
    }

    fun loadReminders() {
        if (authToken.isBlank()) {
            errorMessage.value = "Token de autenticación no disponible. Inicia sesión."
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            val result = repository.getReminders(authToken)

            isLoading.value = false

            result.onSuccess { apiReminders ->
                reminders.clear()
                reminders.addAll(apiReminders.map {
                    UiReminder(
                        id = it.id,
                        title = it.title,
                        description = it.description,
                        reminderDateTime = it.reminderDateTime,
                        frequency = it.frequency,
                        userId = it.userId,
                        isDone = false
                    )
                })
            }.onFailure { error ->
                errorMessage.value = error.message ?: "Error desconocido al cargar recordatorios."
            }
        }
    }

    fun createReminder(
        title: String,
        description: String?,
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        frequency: String?
    ) {
        if (authToken.isBlank()) {
            errorMessage.value = "Token de autenticación no disponible. Inicia sesión."
            return
        }

        viewModelScope.launch {
            errorMessage.value = null

            val localDateTime = LocalDateTime.of(year, month + 1, day, hour, minute)
            val offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.UTC)
            val isoTimestamp = offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

            val request = ReminderCreateRequest(title, description, isoTimestamp, frequency)
            val result = repository.createReminder(authToken, request)

            result.onSuccess { newReminder ->
                reminders.add(UiReminder(
                    id = newReminder.id,
                    title = newReminder.title,
                    description = newReminder.description,
                    reminderDateTime = newReminder.reminderDateTime,
                    frequency = newReminder.frequency,
                    userId = newReminder.userId,
                    isDone = false
                ))
            }.onFailure { error ->
                errorMessage.value = error.message ?: "Error desconocido al crear recordatorio."
            }
        }
    }

    fun updateReminder(uiReminder: UiReminder) {
        if (authToken.isBlank()) {
            errorMessage.value = "Token de autenticación no disponible. Inicia sesión."
            return
        }

        viewModelScope.launch {
            errorMessage.value = null

            val request = ReminderUpdateRequest(
                title = uiReminder.title,
                description = uiReminder.description,
                reminderDateTime = uiReminder.reminderDateTime,
                frequency = uiReminder.frequency
            )

            val result = repository.updateReminder(authToken, uiReminder.id, request)

            result.onSuccess { updatedApiReminder ->
                val index = reminders.indexOfFirst { it.id == updatedApiReminder.id }
                if (index != -1) {
                    reminders[index] = UiReminder(
                        id = updatedApiReminder.id,
                        title = updatedApiReminder.title,
                        description = updatedApiReminder.description,
                        reminderDateTime = updatedApiReminder.reminderDateTime,
                        frequency = updatedApiReminder.frequency,
                        userId = updatedApiReminder.userId,
                        isDone = uiReminder.isDone
                    )
                }
            }.onFailure { error ->
                errorMessage.value = error.message ?: "Error desconocido al actualizar recordatorio."
            }
        }
    }

    fun deleteReminder(reminderId: Int) {
        if (authToken.isBlank()) {
            errorMessage.value = "Token de autenticación no disponible. Inicia sesión."
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            val result = repository.deleteReminder(authToken, reminderId)
            isLoading.value = false

            result.onSuccess {
                reminders.removeIf { it.id == reminderId }
            }.onFailure { error ->
                errorMessage.value = error.message ?: "Error desconocido al eliminar recordatorio."
            }
        }
    }

    fun toggleReminderDone(reminderId: Int, isChecked: Boolean) {
        val index = reminders.indexOfFirst { it.id == reminderId }
        if (index != -1) {
            val updatedReminder = reminders[index].copy(isDone = isChecked)
            reminders[index] = updatedReminder
        }
    }

    fun formatReminderTime(dateTimeString: String): String {
        return try {
            val dateTime = OffsetDateTime.parse(dateTimeString)
            dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        } catch (e: DateTimeParseException) {
            "Hora inválida"
        } catch (e: Exception) {
            "N/A"
        }
    }
}