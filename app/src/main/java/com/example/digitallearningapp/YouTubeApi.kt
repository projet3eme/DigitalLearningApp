package com.example.digitallearningapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApi {

    // جلب فيديوهات من Playlist
    @GET("playlistItems")
    fun getVideosFromPlaylist(
        @Query("key") apiKey: String,
        @Query("playlistId") playlistId: String,
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 20
    ): Call<YouTubeResponse>

    // جلب فيديوهات فردية من القناة
    @GET("search")
    fun getVideosFromChannel(
        @Query("key") apiKey: String,
        @Query("channelId") channelId: String,
        @Query("part") part: String = "snippet",
        @Query("order") order: String = "date",
        @Query("maxResults") maxResults: Int = 20
    ): Call<YouTubeResponse>
}