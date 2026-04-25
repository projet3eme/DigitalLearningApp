package com.example.digitallearningapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val id: Int = 0,
    val name: String = "",
    @SerialName("playlist_id")
    val playlistId: String = "",
    val level: String = "",
    val year: String = ""

)