package com.example.digitallearningapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val title: String,
    @SerialName("video_id")
    val videoId: String,
    val thumbnail: String? = null,
    @SerialName("video_url")
    val videoUrl: String? = null,
    @SerialName("playlist_id")
    val playlistId: String? = null,
    @SerialName("subject_id")
    val subjectId: Int? = null
)
