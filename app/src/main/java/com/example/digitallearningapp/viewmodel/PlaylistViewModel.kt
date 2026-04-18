package com.example.digitallearningapp.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitallearningapp.model.Video
import com.example.digitallearningapp.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PlaylistViewModel : ViewModel() {

    private val _videos = mutableStateOf<List<Video>>(emptyList())
    val videos: State<List<Video>> = _videos

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    /**
     * جلب الفيديوهات باستخدام معرف قائمة التشغيل (Playlist Items) بشكل مباشر.
     */
    fun fetchVideos(playlistId: String, apiKey: String) {
        // Log message as requested for debugging
        Log.d("DEBUG_YT", "Fetching playlist items for ID: $playlistId")

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // استدعاء playlistItems().list("snippet") عبر Retrofit
                // تم ضبط maxResults إلى 50 كما هو مطلوب
                val response = RetrofitInstance.api.getVideosFromPlaylist(
                    apiKey = apiKey,
                    playlistId = playlistId,
                    part = "snippet",
                    maxResults = 50
                )

                Log.d("DEBUG_YT", "Items found: ${response.items.size}")

                val fetchedVideos = response.items.mapNotNull { item ->
                    // استخراج videoId من resourceId داخل الـ snippet
                    val vId = item.snippet.resourceId?.videoId
                    if (vId != null) {
                        Video(
                            title = item.snippet.title,
                            videoId = vId,
                            thumbnail = item.snippet.thumbnails?.medium?.url
                        )
                    } else null
                }

                _videos.value = fetchedVideos

                if (fetchedVideos.isEmpty()) {
                    Log.e("YT_ERROR", "Playlist items list is empty.")
                } else {
                    Log.d("DEBUG_YT", "Successfully mapped ${fetchedVideos.size} videos from Playlist")
                }

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("DEBUG_YT", "HTTP Error ${e.code()}: $errorBody")
                _errorMessage.value = "حدث خطأ في جلب البيانات من القائمة (404/400)"
            } catch (e: Exception) {
                Log.e("DEBUG_YT", "Unexpected Error: ${e.message}")
                _errorMessage.value = "يرجى التحقق من اتصال الإنترنت"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
