package com.example.digitallearningapp.data.repository

import android.util.Log
import com.example.digitallearningapp.model.Subject
import com.example.digitallearningapp.model.Video
import com.example.digitallearningapp.network.YouTubeApi
import com.example.digitallearningapp.network.RetrofitInstance
import com.example.digitallearningapp.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class YouTubeRepository(private val api: YouTubeApi = RetrofitInstance.api) {


    suspend fun getSubjects(level: String, year: String): List<Subject> {
        return try {
            Log.d("SUPABASE", "Fetching: level=$level, year=$year")
            val result = SupabaseClient.client.postgrest["subjects"]
                .select {
                    filter {
                        eq("level", level)
                        eq("year", year)
                    }
                }
                .decodeList<Subject>()
            Log.d("SUPABASE", "Result: ${result.size} subjects")
            result
        } catch (e: Exception) {
            Log.e("SUPABASE", "Error: ${e.message}")
            emptyList()
        }
    }


    suspend fun getVideosFromPlaylist(playlistId: String, apiKey: String): List<Video> {
        return try {
            val response = api.getVideosFromPlaylist(
                apiKey = apiKey,
                playlistId = playlistId,
                part = "snippet",
                maxResults = 50
            )
            response.items.mapNotNull { item ->
                val vId = item.snippet.resourceId?.videoId
                if (vId != null) Video(
                    title = item.snippet.title,
                    videoId = vId,
                    thumbnail = item.snippet.thumbnails?.medium?.url
                ) else null
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
