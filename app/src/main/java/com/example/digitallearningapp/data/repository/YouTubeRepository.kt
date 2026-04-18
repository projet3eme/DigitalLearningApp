package com.example.digitallearningapp.data.repository

import com.example.digitallearningapp.model.PlaylistItem
import com.example.digitallearningapp.network.YouTubeApi

class YouTubeRepository(private val api: YouTubeApi) {

    suspend fun getPlaylists(): List<PlaylistItem> {
        return api.getPlaylists(
            channelId = TODO(),
            apiKey = TODO(),
            part = TODO(),
            maxResults = TODO()
        ).items
    }
}