package com.example.digitallearningapp.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitallearningapp.model.SupabaseClient
import com.example.digitallearningapp.model.Video
import com.example.digitallearningapp.network.RetrofitInstance
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PlaylistViewModel : ViewModel() {

    private val _videos = mutableStateOf<List<Video>>(emptyList())
    val videos: State<List<Video>> = _videos

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun fetchVideos(playlistId: String, apiKey: String) {
        Log.d("DEBUG_YT", "Fetching for ID: $playlistId")

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                if (playlistId.startsWith("http")) {
                    _videos.value = listOf(
                        Video(
                            title = "درس",
                            videoId = playlistId,
                            thumbnail = null
                        )
                    )
                    _isLoading.value = false
                    return@launch
                }

                val response = RetrofitInstance.api.getVideosFromPlaylist(
                    apiKey = apiKey,
                    playlistId = playlistId,
                    part = "snippet",
                    maxResults = 50
                )

                val fetchedVideos = response.items.mapNotNull { item ->
                    val vId = item.snippet.resourceId?.videoId
                    if (vId != null) Video(
                        title = item.snippet.title,
                        videoId = vId,
                        thumbnail = item.snippet.thumbnails?.medium?.url
                    ) else null
                }

                _videos.value = fetchedVideos

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("DEBUG_YT", "HTTP Error ${e.code()}: $errorBody")
                _errorMessage.value = "حدث خطأ في جلب البيانات (404/400)"
            } catch (e: Exception) {
                Log.e("DEBUG_YT", "Unexpected Error: ${e.message}")
                _errorMessage.value = "يرجى التحقق من اتصال الإنترنت"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchVideosFromSupabase(playlistId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val videos = SupabaseClient.client.postgrest["videos"]
                    .select {
                        filter { eq("playlist_id", playlistId) }
                    }
                    .decodeList<Video>()

                if (videos.isNotEmpty()) {
                    _videos.value = videos
                } else {
                    fetchVideos(
                        playlistId = playlistId,
                        apiKey = "AIzaSyC3VzbxUXNJHp_B3xjuSFUpjr3FzWFLSBg"
                    )
                }
            } catch (e: Exception) {
                fetchVideos(
                    playlistId = playlistId,
                    apiKey = "AIzaSyC3VzbxUXNJHp_B3xjuSFUpjr3FzWFLSBg"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}