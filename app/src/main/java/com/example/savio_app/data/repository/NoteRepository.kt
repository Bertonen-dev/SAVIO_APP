package com.example.savio_app.data.repository

import com.example.savio_app.data.model.Note
import com.example.savio_app.data.model.NoteCreateRequest
import com.example.savio_app.data.network.RetrofitInstance
import com.example.savio_app.data.model.NoteUpdateRequest
import com.example.savio_app.data.model.EditNoteResponse

class NoteRepository {

    suspend fun getNotes(token: String, idusuario: String): List<Note> {
        return RetrofitInstance.noteApi.getNotes(token, idusuario)
    }

    suspend fun createNote(token: String, title: String, content: String?, idusuario: String): Note {
        val request = NoteCreateRequest(titulo = title, contenido = content, idusuario = idusuario)
        val response = RetrofitInstance.noteApi.createNote(token, request)
        return response.nota
    }

    suspend fun deleteNote(token: String, noteId: Int): Boolean {
        val response = RetrofitInstance.noteApi.deleteNote(token, noteId)
        return response.isSuccessful
    }

    suspend fun editNote(token: String, noteId: Int, title: String, content: String): Note {
        val request = NoteUpdateRequest(titulo = title, contenido = content)
        val response: EditNoteResponse = RetrofitInstance.noteApi.editNote(token, noteId, request)
        return response.nota
    }
}
