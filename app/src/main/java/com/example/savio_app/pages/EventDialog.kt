package com.example.savio_app.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.savio_app.data.model.Evento
import com.example.savio_app.components.DateTimePickerField
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDialog(
    isEditing: Boolean = false,
    evento: Evento? = null,
    onDismiss: () -> Unit,
    onConfirm: (titulo: String, descripcion: String, fechaInicio: String, fechaFin: String) -> Unit
) {
    var titulo by remember { mutableStateOf(evento?.titulo ?: "") }
    var descripcion by remember { mutableStateOf(evento?.descripcion ?: "") }
    var fechaInicio by remember {
        mutableStateOf(evento?.fecha_inicio ?: LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
    }
    var fechaFin by remember {
        mutableStateOf(evento?.fecha_fin ?: LocalDateTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "Editar Evento" else "Nuevo Evento") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                DateTimePickerField(
                    value = fechaInicio,
                    onValueChange = { fechaInicio = it },
                    label = "Fecha y hora de inicio",
                    modifier = Modifier.fillMaxWidth()
                )

                DateTimePickerField(
                    value = fechaFin,
                    onValueChange = { fechaFin = it },
                    label = "Fecha y hora de fin",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (titulo.isNotBlank()) {
                        val formatterInput = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                        val formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

                        val fechaInicioApi = LocalDateTime.parse(fechaInicio, formatterInput).format(formatterOutput)
                        val fechaFinApi = LocalDateTime.parse(fechaFin, formatterInput).format(formatterOutput)

                        onConfirm(titulo, descripcion, fechaInicioApi, fechaFinApi)
                    }
                }
            ) {
                Text(if (isEditing) "Actualizar" else "Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
