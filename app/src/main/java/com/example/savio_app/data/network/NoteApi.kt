package com.example.savio_app.data.network

import com.example.savio_app.data.model.EditNoteResponse
import com.example.savio_app.data.model.Note
import com.example.savio_app.data.model.NoteCreateRequest
import com.example.savio_app.data.model.NoteUpdateRequest
import retrofit2.Response
import retrofit2.http.*

interface NoteAPI {

    @GET("notas/{idusuario}")
    suspend fun getNotes(
        @Header("Authorization") token: String,
        @Path("idusuario") idusuario: String
    ): List<Note>

    @POST("notas")
    suspend fun createNote(
        @Header("Authorization") token: String,
        @Body noteCreateRequest: NoteCreateRequest
    ): CreateNoteResponse

    @DELETE("notas/{id}")
    suspend fun deleteNote(
        @Header("Authorization") token: String,
        @Path("id") noteId: Int
    ): Response<Unit>

    @PUT("notas/{idnota}")
    suspend fun editNote(
        @Header("Authorization") token: String,
        @Path("idnota") noteId: Int,
        @Body noteUpdateRequest: NoteUpdateRequest
    ): EditNoteResponse

}
