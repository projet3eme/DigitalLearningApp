package com.example.digitallearningapp.network

import com.example.digitallearningapp.model.*
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApi {

    @GET("playlists")
    suspend fun getPlaylists(
        @Query("channelId") channelId: String,
        @Query("key") apiKey: String,
        @Query("part") part: String = "snippet,contentDetails",
        @Query("maxResults") maxResults: Int = 50
    ): PlaylistResponse

    @GET("playlistItems")
    suspend fun getVideosFromPlaylist(
        @Query("key") apiKey: String,
        @Query("playlistId") playlistId: String,
        @Query("part") part: String = "snippet,contentDetails",
        @Query("maxResults") maxResults: Int = 50
    ): VideoResponse

    @GET("search")
    suspend fun searchVideos(
        @Query("key") apiKey: String,
        @Query("channelId") channelId: String,
        @Query("part") part: String = "snippet",
        @Query("order") order: String = "date",
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 50
    ): SearchResponse

}
