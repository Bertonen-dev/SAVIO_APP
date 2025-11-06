package com.example.savio_app.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.savio_app.data.model.Note
import com.example.savio_app.data.repository.NoteRepository
import com.example.savio_app.data.local.SessionManager
import kotlinx.coroutines.launch

@Composable
fun NotesPage(
    isDarkMode: Boolean,
    sessionManager: SessionManager,
    noteRepository: NoteRepository
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var notes by remember { mutableStateOf<List<Note>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }

    var showEditDialog by remember { mutableStateOf(false) }
    var selectedNote by remember { mutableStateOf<Note?>(null) }

    LaunchedEffect(Unit) {
        val tokenRaw = sessionManager.getToken()
        val idusuario = sessionManager.getIdUsuario()
        if (tokenRaw == null || idusuario == null) {
            errorMessage = "Usuario no autenticado"
            isLoading = false
            return@LaunchedEffect
        }

        val token = if (tokenRaw.startsWith("Bearer ")) tokenRaw else "Bearer $tokenRaw"
        Log.d("NotesPage", "Token usado: $token")

        try {
            notes = noteRepository.getNotes(token, idusuario)
            Log.d("NotesPage", "Notas recibidas: ${notes.size}")
        } catch (e: Exception) {
            errorMessage = e.message ?: "Error al cargar notas"
            Log.e("NotesPage", "Error al cargar notas", e)
        } finally {
            isLoading = false
        }
    }

    fun deleteNote(note: Note) {
        coroutineScope.launch {
            val tokenRaw = sessionManager.getToken()
            if (tokenRaw == null) {
                Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
                return@launch
            }
            val token = if (tokenRaw.startsWith("Bearer ")) tokenRaw else "Bearer $tokenRaw"

            try {
                val success = noteRepository.deleteNote(token, note.id)
                if (success) {
                    notes = notes.filter { it.id != note.id }
                    Toast.makeText(context, "Nota eliminada", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error al eliminar nota", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun editNote(note: Note) {
        selectedNote = note
        showEditDialog = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkMode) Color(0xFF121212) else Color(0xFFF2F2F2))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "Notas",
                style = MaterialTheme.typography.headlineLarge,
                color = if (isDarkMode) Color.White else Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                notes.isEmpty() -> {
                    Text(
                        text = "No hay notas",
                        color = if (isDarkMode) Color.LightGray else Color.DarkGray,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else -> {
                    LazyColumn {
                        items(notes) { note ->
                            NoteItem(
                                note = note,
                                isDarkMode = isDarkMode,
                                onEdit = { editNote(it) },
                                onDelete = { deleteNote(it) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showCreateDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+", style = MaterialTheme.typography.headlineMedium)
        }
    }

    if (showCreateDialog) {
        CreateNoteDialog(
            isDarkMode = isDarkMode,
            onSaveNote = { title, content ->
                coroutineScope.launch {
                    val tokenRaw = sessionManager.getToken()
                    val idusuario = sessionManager.getIdUsuario()
                    if (tokenRaw == null || idusuario == null) {
                        Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
                        showCreateDialog = false
                        return@launch
                    }
                    val token = if (tokenRaw.startsWith("Bearer ")) tokenRaw else "Bearer $tokenRaw"
                    try {
                        val newNote = noteRepository.createNote(token, title, content, idusuario)
                        notes = notes + newNote
                        Toast.makeText(context, "Nota guardada", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    } finally {
                        showCreateDialog = false
                    }
                }
            },
            onDismiss = {
                showCreateDialog = false
            }
        )
    }

    if (showEditDialog && selectedNote != null) {
        EditNoteDialog(
            isDarkMode = isDarkMode,
            initialNote = selectedNote!!,
            onSave = { newTitle, newContent ->
                coroutineScope.launch {
                    val tokenRaw = sessionManager.getToken()
                    if (tokenRaw == null) return@launch
                    val token = if (tokenRaw.startsWith("Bearer ")) tokenRaw else "Bearer $tokenRaw"

                    try {
                        val updatedNote = noteRepository.editNote(token, selectedNote!!.id, newTitle, newContent)
                        notes = notes.map {
                            if (it.id == selectedNote!!.id) updatedNote else it
                        }
                        Toast.makeText(context, "Nota actualizada", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    } finally {
                        showEditDialog = false
                        selectedNote = null
                    }
                }
            },
            onDismiss = {
                showEditDialog = false
                selectedNote = null
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    note: Note,
    isDarkMode: Boolean,
    onEdit: (Note) -> Unit,
    onDelete: (Note) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isDarkMode) Color.DarkGray else Color.White,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = { showMenu = true }
            )
    ) {
        Text(
            note.title ?: "",
            style = MaterialTheme.typography.titleMedium,
            color = if (isDarkMode) Color.White else Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            note.content ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDarkMode) Color.LightGray else Color.DarkGray
        )

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("Editar") },
                onClick = {
                    showMenu = false
                    onEdit(note)
                }
            )
            DropdownMenuItem(
                text = { Text("Borrar") },
                onClick = {
                    showMenu = false
                    onDelete(note)
                }
            )
        }
    }
}

@Composable
fun CreateNoteDialog(
    isDarkMode: Boolean,
    onSaveNote: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var noteTitle by remember { mutableStateOf(TextFieldValue("")) }
    var noteContent by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Crear Nota", color = if (isDarkMode) Color.White else Color.Black)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = noteTitle,
                    onValueChange = { noteTitle = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = noteContent,
                    onValueChange = { noteContent = it },
                    label = { Text("Contenido") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (noteTitle.text.isNotBlank() && noteContent.text.isNotBlank()) {
                    onSaveNote(noteTitle.text, noteContent.text)
                }
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        containerColor = if (isDarkMode) Color(0xFF333333) else Color.White
    )
}

@Composable
fun EditNoteDialog(
    isDarkMode: Boolean,
    initialNote: Note,
    onSave: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(TextFieldValue(initialNote.title ?: "")) }
    var content by remember { mutableStateOf(TextFieldValue(initialNote.content ?: "")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Editar Nota", color = if (isDarkMode) Color.White else Color.Black)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Contenido") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (title.text.isNotBlank()) {
                    onSave(title.text, content.text)
                }
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        containerColor = if (isDarkMode) Color(0xFF333333) else Color.White
    )
}
