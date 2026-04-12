package com.example.digitallearningapp

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    // ضع هنا القيم الخاصة بك
    private val CHANNEL_PRIMARY = "UC6r0ffiOauGJD0dbyYwlNtA"
    private val CHANNEL_SECONDARY = "UCl7GUW9Mnug3UzNjg2ulk7A"
    private val API_KEY = "AIzaSyC3VzbxUXNJHp_B3xjuSFUpjr3FzWFLSBg"

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/youtube/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(YouTubeApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                var screen by remember { mutableStateOf("splash") }
                var level by remember { mutableStateOf("") }
                var subject by remember { mutableStateOf("") }
                var playlists by remember { mutableStateOf(listOf<Playlist>()) }
                var videos by remember { mutableStateOf(listOf<Video>()) }
                var selectedVideoId by remember { mutableStateOf<String?>(null) }
                var isLoading by remember { mutableStateOf(false) }

                when (screen) {
                    "splash" -> SplashScreen { screen = "levels" }

                    "levels" -> LevelScreen { chosenLevel ->
                        level = chosenLevel
                        screen = "subjects"
                    }

                    "subjects" -> SubjectScreen(level) { chosenSubject ->
                        subject = chosenSubject
                        isLoading = true
                        playlists = emptyList()

                        CoroutineScope(Dispatchers.IO).launch {
                            val fetched = fetchPlaylists(level, subject)
                            playlists = fetched
                            Log.d("DEBUG_API", "قوائم التشغيل بعد التصفية: ${playlists.size}")
                            isLoading = false
                            screen = "playlists"
                        }
                    }

                    "playlists" -> {
                        if (isLoading) Loader()
                        else PlaylistScreen(playlists) { playlistId ->
                            isLoading = true
                            videos = emptyList()

                            CoroutineScope(Dispatchers.IO).launch {
                                val fetchedVideos = fetchVideos(playlistId)
                                videos = fetchedVideos
                                Log.d("DEBUG_API", "عدد الفيديوهات المسترجعة: ${videos.size}")
                                isLoading = false
                                screen = "videos"
                            }
                        }
                    }

                    "videos" -> {
                        if (isLoading) Loader()
                        else VideoScreen(videos) { selectedVideoId = it }
                    }
                }

                selectedVideoId?.let { videoId ->
                    VideoPlayer(videoId) { selectedVideoId = null }
                }
            }
        }
    }

    // ======== API Calls ========
    private suspend fun fetchPlaylists(level: String, subject: String): List<Playlist> {
        return try {
            val channelId = if (level.contains("الابتدائي")) CHANNEL_PRIMARY else CHANNEL_SECONDARY
            val response = api.getPlaylists(channelId, API_KEY)
            val items = response.items ?: emptyList()

            // فلترة مرنة جدًا
            val filtered = items.filter { item ->
                var title = item.snippet.title.lowercase()
                    .replace("\\s|[-|,،:_]".toRegex(), "")
                    .replace("ال", "")
                    .replace("إ", "ا").replace("أ", "ا").replace("آ", "ا")

                var cleanLevel = level.lowercase()
                    .replace("\\s".toRegex(), "")
                    .replace("ال", "")
                    .replace("إ", "ا").replace("أ", "ا").replace("آ", "ا")

                var cleanSubject = subject.lowercase()
                    .replace("\\s".toRegex(), "")
                    .replace("ال", "")
                    .replace("إ", "ا").replace("أ", "ا").replace("آ", "ا")

                title.contains(cleanLevel) && title.contains(cleanSubject)
            }

            filtered.map {
                Playlist(
                    id = it.id,
                    title = it.snippet.title,
                    thumbnails = it.snippet.thumbnails?.medium
                )
            }
        } catch (e: Exception) {
            Log.e("DEBUG_API", "فشل جلب قوائم التشغيل: ${e.message}")
            emptyList()
        }
    }

    private suspend fun fetchVideos(playlistId: String): List<Video> {
        return try {
            Log.d("DEBUG_API", "جلب الفيديوهات من Playlist: $playlistId")

            // استدعاء API
            val response = api.getVideosFromPlaylist(API_KEY, playlistId)
            val items = response.items ?: emptyList()

            // طباعة كل الفيديوهات للتأكد
            items.forEach {
                val vid = it.contentDetails?.videoId ?: it.snippet.resourceId?.videoId ?: it.id.videoId
                Log.d("DEBUG_API", "Found video: ${it.snippet.title}, videoId: $vid")
            }

            // تحويل الفيديوهات إلى قائمة Video
            val videosList = items.mapNotNull { item ->
                val videoId = item.contentDetails?.videoId
                    ?: item.snippet.resourceId?.videoId
                    ?: item.id.videoId
                if (videoId.isNullOrEmpty()) return@mapNotNull null

                Video(
                    title = item.snippet.title ?: "بدون عنوان",
                    videoId = videoId,
                    thumbnail = item.snippet.thumbnails?.medium?.url ?: ""
                )
            }

            videosList

        } catch (e: Exception) {
            Log.e("DEBUG_API", "فشل جلب الفيديوهات: ${e.message}")
            emptyList()
        }
    } 
}