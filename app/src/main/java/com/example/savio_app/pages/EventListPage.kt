package com.example.savio_app.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.savio_app.data.model.Evento
import com.example.savio_app.viewmodel.EventViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListPage(viewModel: EventViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedEvento by remember { mutableStateOf<Evento?>(null) }
    val eventos by viewModel.eventos.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val operationResult by viewModel.operationResult.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadEventos()
    }

    LaunchedEffect(operationResult) {
        operationResult?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedEvento = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Evento")
            }
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(eventos, key = { it.idevento }) { evento ->
                    EventoCard(
                        evento = evento,
                        onEdit = {
                            selectedEvento = evento
                            showDialog = true
                        },
                        onDelete = {
                            scope.launch {
                                viewModel.deleteEvento(evento.idevento)
                            }
                        }
                    )
                }
            }

            if (eventos.isEmpty()) {
                Text(
                    text = "No hay eventos",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

    if (showDialog) {
        EventDialog(
            isEditing = selectedEvento != null,
            evento = selectedEvento,
            onDismiss = { showDialog = false },
            onConfirm = { titulo, descripcion, fechaInicio, fechaFin ->
                scope.launch {
                    if (selectedEvento != null) {
                        viewModel.updateEvento(
                            selectedEvento!!.idevento,
                            titulo,
                            descripcion,
                            fechaInicio,
                            fechaFin
                        )
                    } else {
                        viewModel.createEvento(titulo, descripcion, fechaInicio, fechaFin)
                    }
                    showDialog = false
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventoCard(
    evento: Evento,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val formatearFecha = { fecha: String ->
        try {
            val fechaDateTime = LocalDateTime.parse(fecha)
            "${fechaDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))} a las ${fechaDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
        } catch (e: Exception) {
            fecha
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = evento.titulo,
                    style = MaterialTheme.typography.titleMedium
                )
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = evento.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Text(
                    text = "Inicio: ${formatearFecha(evento.fecha_inicio)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Fin: ${formatearFecha(evento.fecha_fin)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
