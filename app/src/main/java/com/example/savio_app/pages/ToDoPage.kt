package com.example.savio_app.pages

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.savio_app.data.local.SessionManager
import com.example.savio_app.viewmodels.ToDoViewModel
import com.example.savio_app.viewmodels.UiReminder
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoPage(
    viewModel: ToDoViewModel = viewModel(),
    navController: NavController
) {
    val reminders = viewModel.reminders
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    var showCreateDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        val sessionManager = SessionManager(context)
        val realToken = sessionManager.getToken()

        if (realToken != null) {
            viewModel.setAuthToken(realToken)
        } else {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "Tu sesión ha expirado o no has iniciado sesión. Por favor, inicia sesión.",
                    actionLabel = "Iniciar Sesión",
                    duration = SnackbarDuration.Long
                )
                navController.navigate("loginPage") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Recordatorios") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir recordatorio")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            errorMessage?.let { message ->
                AlertDialog(
                    onDismissRequest = { viewModel.errorMessage.value = null },
                    title = { Text("Error") },
                    text = { Text(message) },
                    confirmButton = {
                        TextButton(onClick = { viewModel.errorMessage.value = null }) {
                            Text("Aceptar")
                        }
                    }
                )
            }

            if (!isLoading && reminders.isEmpty()) {
                Spacer(Modifier.height(32.dp))
                Text(
                    text = "No hay recordatorios. ¡Crea uno nuevo!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(top = 16.dp)
                ) {
                    items(reminders, key = { it.id }) { reminder ->
                        ReminderCard(
                            reminder = reminder,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateReminderDialog(
            onDismiss = { showCreateDialog = false },
            onSave = { title, description, year, month, day, hour, minute, frequency ->
                viewModel.createReminder(title, description, year, month, day, hour, minute, frequency)
                showCreateDialog = false
            },
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
fun ReminderCard(reminder: UiReminder, viewModel: ToDoViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = reminder.isDone,
                onCheckedChange = { isChecked ->
                    viewModel.toggleReminderDone(reminder.id, isChecked)
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                reminder.description?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(
                onClick = { viewModel.deleteReminder(reminder.id) },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar recordatorio")
            }
        }
    }
}

@Composable
fun CreateReminderDialog(
    onDismiss: () -> Unit,
    onSave: (String, String?, Int, Int, Int, Int, Int, String?) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    var selectedYear by remember { mutableIntStateOf(calendar.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableIntStateOf(calendar.get(Calendar.MONTH)) }
    var selectedDay by remember { mutableIntStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }
    var selectedHour by remember { mutableIntStateOf(calendar.get(Calendar.HOUR_OF_DAY)) }
    var selectedMinute by remember { mutableIntStateOf(calendar.get(Calendar.MINUTE)) }

    var selectedDateDisplay by remember { mutableStateOf("") }
    var selectedTimeDisplay by remember { mutableStateOf("") }

    var frequency by remember { mutableStateOf("") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                selectedYear = year
                selectedMonth = month
                selectedDay = dayOfMonth
                selectedDateDisplay = "${String.format("%02d", dayOfMonth)}/${String.format("%02d", month + 1)}/$year"
            }, selectedYear, selectedMonth, selectedDay
        )
    }

    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                selectedHour = hourOfDay
                selectedMinute = minute
                selectedTimeDisplay = String.format("%02d:%02d", hourOfDay, minute)
            }, selectedHour, selectedMinute, true
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Recordatorio") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = selectedDateDisplay,
                    onValueChange = { /* Not directly editable */ },
                    label = { Text("Fecha *") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(Icons.Default.Add, contentDescription = "Seleccionar fecha")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = selectedTimeDisplay,
                    onValueChange = { /* Not directly editable */ },
                    label = { Text("Hora *") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { timePickerDialog.show() }) {
                            Icon(Icons.Default.Add, contentDescription = "Seleccionar hora")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = frequency,
                    onValueChange = { frequency = it },
                    label = { Text("Frecuencia (opcional)") },
                    placeholder = { Text("ej. diario, semanal, cada 15 días") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val isDateTimeSelected = selectedDateDisplay.isNotBlank() && selectedTimeDisplay.isNotBlank()
                    if (title.isNotBlank() && isDateTimeSelected) {
                        val finalDescription = description.ifBlank { null }
                        val finalFrequency = frequency.ifBlank { null }
                        onSave(title, finalDescription, selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, finalFrequency)
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Por favor, completa el título, la fecha y la hora.",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}