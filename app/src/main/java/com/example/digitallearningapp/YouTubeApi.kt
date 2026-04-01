package com.example.digitallearningapp

import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApi {

    @GET("playlists")
    suspend fun getPlaylists(
        @Query("channelId") channelId: String,
        @Query("key") apiKey: String,
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 50
    ): PlaylistsResponse

    @GET("playlistItems")
    suspend fun getVideosFromPlaylist(
        @Query("key") apiKey: String,
        @Query("playlistId") playlistId: String,
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 50
    ): YouTubeResponse
}