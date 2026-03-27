package com.example.digitallearningapp

import android.os.Bundle
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private val API_KEY = "YOUR API KEY"
    private val ELEMENTARY_PLAYLIST_ID: String? = null // ضع Playlist ID إذا متاح
    private val ELEMENTARY_CHANNEL_ID = "UC6r0ffiOauGJD0dbyYwlNtA"
    private val MIDDLE_HIGH_PLAYLIST_ID: String? = null // ضع Playlist ID إذا متاح
    private val MIDDLE_HIGH_CHANNEL_ID = "UCl7GUW9Mnug3UzNjg2ulk7A"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {

                val videos = remember { mutableStateListOf<Video>() }
                var selectedVideoId by remember { mutableStateOf<String?>(null) }
                var currentScreen by remember { mutableStateOf("Splash") }
                var selectedPlaylistId by remember { mutableStateOf<String?>(null) }
                var selectedChannelId by remember { mutableStateOf("") }
                var isLoading by remember { mutableStateOf(false) }

                when (currentScreen) {

                    "Splash" -> SplashScreen { currentScreen = "Level" }

                    "Level" -> LevelSelectionScreen { level ->
                        videos.clear()
                        when (level) {
                            "ابتدائي" -> {
                                selectedPlaylistId = ELEMENTARY_PLAYLIST_ID
                                selectedChannelId = ELEMENTARY_CHANNEL_ID
                            }
                            "ثانوي" -> {
                                selectedPlaylistId = MIDDLE_HIGH_PLAYLIST_ID
                                selectedChannelId = MIDDLE_HIGH_CHANNEL_ID
                            }
                        }

                        isLoading = true
                        currentScreen = "Videos"

                        fetchVideosFlexible(API_KEY, selectedPlaylistId, selectedChannelId, videos) {
                            isLoading = false
                        }
                    }

                    "Videos" -> {
                        if (isLoading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = androidx.compose.ui.Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            VideoListScreen(videos) { videoId ->
                                selectedVideoId = videoId
                            }
                        }
                    }
                }

                selectedVideoId?.let { videoId ->
                    VideoWebView(videoId) {
                        selectedVideoId = null
                    }
                }
            }
        }
    }

    private fun fetchVideosFlexible(
        apiKey: String,
        playlistId: String?,
        channelId: String,
        videos: MutableList<Video>,
        onComplete: () -> Unit
    ) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(YouTubeApi::class.java)

        val call: Call<YouTubeResponse> = if (!playlistId.isNullOrEmpty()) {
            api.getVideosFromPlaylist(apiKey, playlistId)
        } else {
            api.getVideosFromChannel(apiKey, channelId)
        }

        call.enqueue(object : Callback<YouTubeResponse> {
            override fun onResponse(call: Call<YouTubeResponse>, response: Response<YouTubeResponse>) {
                if (response.isSuccessful) {
                    response.body()?.items?.forEach { item ->
                        val videoId = item.snippet.resourceId?.videoId ?: item.id.videoId
                        val title = item.snippet.title
                        val thumbnail = item.snippet.thumbnails.medium.url

                        if (!videoId.isNullOrEmpty()) {
                            videos.add(Video(title, videoId, thumbnail))
                        }
                    }
                }
                onComplete()
            }

            override fun onFailure(call: Call<YouTubeResponse>, t: Throwable) {
                onComplete()
            }
        })
    }
}

@Composable
fun SplashScreen(onStart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("مرحبا بك في التطبيق", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onStart) { Text("ابدأ") }
    }
}

@Composable
fun LevelSelectionScreen(onSelect: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("اختر المستوى", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { onSelect("ابتدائي") }) { Text("ابتدائي") }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { onSelect("ثانوي") }) { Text("متوسط/ثانوي") }
    }
}

@Composable
fun VideoListScreen(videos: List<Video>, onClick: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        items(videos) { video ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clickable { onClick(video.videoId) }
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(video.title)
                    Spacer(modifier = Modifier.height(4.dp))
                    AsyncImage(
                        model = video.thumbnail,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun VideoWebView(videoId: String, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            Text(
                "إغلاق",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onClose() }
            )
        },
        text = {
            AndroidView(
                factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        loadUrl("https://www.youtube.com/embed/$videoId")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )
        }
    )
}