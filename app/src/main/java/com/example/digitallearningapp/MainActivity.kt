package com.example.digitallearningapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.material3.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.activity.compose.BackHandler
import android.content.pm.ActivityInfo

class MainActivity : ComponentActivity() {

    private val CHANNEL_PRIMARY = "UC6r0ffiOauGJD0dbyYwlNtA"
    private val CHANNEL_SECONDARY = "UCl7GUW9Mnug3UzNjg2ulk7A"
    private val API_KEY = "AIzaSyC3VzbxUXNJHp_B3xjuSFUpjr3FzWFLSBg"

    // ✅ تمت إضافته
    private val yearMapping = mapOf(
        "السنة الأولى" to "1", "أولى" to "1", "الاولى" to "1",
        "السنة الثانية" to "2", "ثانية" to "2",
        "السنة الثالثة" to "3", "ثالثة" to "3",
        "السنة الرابعة" to "4", "رابعة" to "4",
        "السنة الخامسة" to "5", "خامسة" to "5"
    )

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/youtube/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(YouTubeApi::class.java)

    // ✅ دالة استخراج السنة
    fun extractYear(title: String): String? {
        val cleanTitle = title.lowercase().replace("\\s|ال|أ|إ|آ".toRegex(), "")
        return yearMapping.entries.firstOrNull { (key, _) ->
            cleanTitle.contains(key.lowercase().replace("\\s|ال|أ|إ|آ".toRegex(), ""))
        }?.value
    }

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

                BackHandler {
                    when (screen) {
                        "videos" -> screen = "playlists"
                        "playlists" -> screen = "subjects"
                        "subjects" -> screen = "levels"
                        "levels" -> screen = "splash"
                    }
                }

                when (screen) {

                    "splash" -> SplashScreen { screen = "levels" }

                    "levels" -> LevelScreen { chosenLevel ->
                        level = chosenLevel
                        screen = "subjects"
                    }

                    "subjects" -> {
                        val split = level.split(" - ")
                        val selectedLevel = split[0]
                        val selectedYear = if (split.size > 1) split[1] else ""

                        SubjectScreen(level) { chosenSubject ->
                            subject = chosenSubject
                            isLoading = true
                            playlists = emptyList()

                            CoroutineScope(Dispatchers.IO).launch {
                                val fetched = fetchPlaylists(selectedLevel, subject, selectedYear)
                                playlists = fetched
                                isLoading = false
                                screen = "playlists"
                            }
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
                                isLoading = false
                                screen = "videos"
                            }
                        }
                    }

                    "videos" -> {
                        if (isLoading) Loader()
                        else VideoScreen(videos) {
                            selectedVideoId = it
                        }
                    }
                }

                selectedVideoId?.let { videoId ->
                    VideoPlayer(videoId) {
                        selectedVideoId = null
                    }
                }
            }
        }
    }

    private suspend fun fetchPlaylists(level: String, subject: String, year: String): List<Playlist> {
        return try {
            val cleanLevel = level.lowercase().replace("\\s|ال".toRegex(), "")
            val cleanSubject = subject.lowercase().replace("\\s|ال".toRegex(), "")
            val cleanYear = extractYear(year) ?: ""

            val channelId = if (cleanLevel.contains("ابتدائي")) CHANNEL_PRIMARY else CHANNEL_SECONDARY

            val response = api.getPlaylists(channelId, API_KEY)
            val items = response.items ?: emptyList()

            val filtered = items.filter { item ->
                val title = item.snippet.title.lowercase().replace("\\s".toRegex(), "")
                val itemYear = extractYear(item.snippet.title)

                title.contains(cleanLevel) &&
                        title.contains(cleanSubject) &&
                        (cleanYear.isEmpty() || itemYear == cleanYear)
            }

            filtered.map {
                Playlist(
                    id = it.id,
                    title = it.snippet.title,
                    thumbnails = it.snippet.thumbnails?.medium
                )
            }

        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun fetchVideos(playlistId: String): List<Video> {
        return try {
            val response = api.getVideosFromPlaylist(API_KEY, playlistId)
            val items = response.items ?: emptyList()

            items.mapNotNull { item ->
                val videoId = item.snippet.resourceId?.videoId ?: item.id
                if (videoId.isNullOrEmpty()) return@mapNotNull null
                Video(
                    title = item.snippet.title,
                    videoId = videoId,
                    thumbnail = item.snippet.thumbnails?.medium?.url ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}