package com.example.savio_app.data.network

import com.example.savio_app.data.model.Note
import com.google.gson.annotations.SerializedName

data class CreateNoteResponse(
    @SerializedName("message") val message: String,
    @SerializedName("nota") val nota: Note
)
