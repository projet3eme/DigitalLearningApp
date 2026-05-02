package com.example.digitallearningapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val title: String,
    val videoId: String,
    val thumbnail: String?
)
