package com.example.digitallearningapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.activity.compose.BackHandler
class MainActivity : ComponentActivity() {

    private val CHANNEL_PRIMARY = "UC6r0ffiOauGJD0dbyYwlNtA"
    private val CHANNEL_SECONDARY = "UCl7GUW9Mnug3UzNjg2ulk7A"
    private val API_KEY = "AIzaSyC3VzbxUXNJHp_B3xjuSFUpjr3FzWFLSBg"

    private val api = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/youtube/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(YouTubeApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            MaterialTheme {

                // ================= STATE =================
                var screen by remember { mutableStateOf("splash") }

                var level by remember { mutableStateOf("") }
                var year by remember { mutableStateOf("") }
                var subject by remember { mutableStateOf("") }

                var playlists by remember { mutableStateOf(listOf<Playlist>()) }
                var videos by remember { mutableStateOf(listOf<Video>()) }

                var selectedVideoId by remember { mutableStateOf<String?>(null) }
                var isLoading by remember { mutableStateOf(false) }

                // ================= NAVIGATION =================

                when (screen) {

                    // 🔵 SPLASH
                    "splash" -> SplashScreen {
                        screen = "levels"
                    }

                    // 🔵 LEVEL + YEAR
                    "levels" -> LevelScreen { chosenLevel, chosenYear ->
                        level = chosenLevel
                        year = chosenYear
                        screen = "subjects"
                    }

                    // 🔵 SUBJECT
                    "subjects" -> SubjectScreen { chosenSubject ->

                        subject = chosenSubject
                        isLoading = true

                        CoroutineScope(Dispatchers.IO).launch {
                            playlists = fetchPlaylists(level, year, subject)
                            isLoading = false
                            screen = "playlists"
                        }
                    }

                    // 🔵 PLAYLISTS
                    "playlists" -> {
                        if (isLoading) {
                            Loader()
                        } else {
                            PlaylistScreen(playlists) { playlistId ->

                                isLoading = true

                                CoroutineScope(Dispatchers.IO).launch {
                                    videos = fetchVideos(playlistId)
                                    isLoading = false
                                    screen = "videos"
                                }
                            }
                        }
                    }

                    // 🔵 VIDEOS
                    "videos" -> {
                        if (isLoading) Loader()
                        else VideoScreen(videos) { videoId ->
                            selectedVideoId = videoId
                        }
                    }
                }

                // ================= VIDEO PLAYER =================

                selectedVideoId?.let { videoId ->
                    VideoPlayer(videoId) {
                        selectedVideoId = null
                    }
                }
                BackHandler {

                    when (screen) {

                        "videos" -> screen = "playlists"

                        "playlists" -> screen = "subjects"

                        "subjects" -> screen = "levels"

                        "levels" -> finish() // خروج من التطبيق

                    }
                }
            }
        }
    }

    // ================= NORMALIZE =================

    fun normalize(text: String): String {
        return text.lowercase()
            .replace("ال", "")
            .replace(" ", "")
            .replace("ة", "ه")
            .replace("أ", "ا")
            .replace("إ", "ا")
            .replace("آ", "ا")
    }// ================= KEYWORDS =================

    fun levelKeywords(level: String) = when {
        level.contains("ابتد") -> listOf("ابتدائي", "ابتدائية")
        level.contains("متوسط") -> listOf("متوسط")
        level.contains("ثانوي") -> listOf("ثانوي")
        else -> listOf(level)
    }

    fun yearKeywords(year: String) = when {
        year.contains("اول") -> listOf("اولى", "السنةالاولى")
        year.contains("ثاني") -> listOf("ثانية", "السنةالثانية")
        year.contains("ثالث") -> listOf("ثالثة", "السنةالثالثة")
        year.contains("رابع") -> listOf("رابعة", "السنةالرابعة")
        year.contains("خامس") -> listOf("خامسة", "السنةالخامسة")
        else -> listOf(year)
    }

    fun subjectKeywords(subject: String) = when {
        subject.contains("عرب") -> listOf("عربية", "اللغةالعربية", "عربي")
        subject.contains("رياض") -> listOf("رياضيات", "الرياضيات")
        subject.contains("اسلام") -> listOf("اسلامية", "التربيةالاسلامية")
        else -> listOf(subject)
    }

    // ================= FETCH PLAYLISTS =================

    private suspend fun fetchPlaylists(
        level: String,
        year: String,
        subject: String
    ): List<Playlist> {

        return try {

            val channelId = if (level.contains("ابتد")) {
                CHANNEL_PRIMARY
            } else {
                CHANNEL_SECONDARY
            }

            val response = api.getPlaylists(channelId, API_KEY)
            val items = response.items ?: emptyList()

            val levels = levelKeywords(level)
            val years = yearKeywords(year)
            val subjects = subjectKeywords(subject)

            items.filter { item ->

                val title = normalize(item.snippet.title)

                val matchLevel = levels.any { title.contains(normalize(it)) }
                val matchYear = years.any { title.contains(normalize(it)) }
                val matchSubject = subjects.any { title.contains(normalize(it)) }

                matchLevel && matchYear && matchSubject
            }.map {

                Playlist(
                    id = it.id,
                    title = it.snippet.title,
                    thumbnails = it.snippet.thumbnails?.medium
                )
            }

        } catch (e: Exception) {
            Log.e("API", "fetchPlaylists error: ${e.message}")
            emptyList()
        }
    }

    // ================= FETCH VIDEOS =================

    private suspend fun fetchVideos(playlistId: String): List<Video> {

        return try {

            Log.e("CHECK", "PLAYLIST ID = $playlistId")

            val response = api.getVideosFromPlaylist(
                apiKey = API_KEY,
                playlistId = playlistId
            )

            val items = response.items ?: emptyList()

            Log.e("CHECK", "ITEMS SIZE = ${items.size}")

            items.mapNotNull { item ->

                val videoId =
                    item.contentDetails?.videoId
                        ?: item.snippet.resourceId?.videoId
                        ?: item.id.videoId

                if (videoId.isNullOrEmpty()) return@mapNotNull null

                Video(
                    title = item.snippet.title,
                    videoId = videoId,
                    thumbnail = item.snippet.thumbnails?.medium?.url
                )
            }

        } catch (e: Exception) {
            Log.e("CHECK", "ERROR = ${e.message}")
            emptyList()
        }
    }
}