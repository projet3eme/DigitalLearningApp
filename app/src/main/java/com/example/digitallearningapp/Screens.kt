
package com.example.digitallearningapp

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import android.webkit.WebChromeClient
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.background
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.statusBarsPadding
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun SplashScreen(onClick: () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "ابدأ رحلتك التعليمية معنا الآن",
            fontSize = 32.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(35.dp))
        Button(onClick = onClick) { Text("ابدأ") }
    }
}

@Composable
fun LevelScreen(onClick: (String) -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("اختر مستواك ", fontSize = 40.sp)
        Spacer(Modifier.height(35.dp))
        listOf("الابتدائي", " المتوسط", "الثانوي").forEach { level ->
            Button(onClick = { onClick(level) }) {
                Text(level)
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun SubjectScreen(level: String, onClick: (String) -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            text = "اختر مادتك - $level",
            fontSize = 28.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(35.dp))
        listOf("تربية اسلامية", "لغة عربية", "رياضيات").forEach { subject ->
            Button(onClick = { onClick(subject) }) {
                Text(subject)
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun PlaylistScreen(list: List<Playlist>, onClick: (String) -> Unit) {
    LazyColumn {
        items(list) { playlist ->
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onClick(playlist.id) }
            ) {
                Column(Modifier.padding(8.dp).statusBarsPadding()) {
                    Text(playlist.title, fontSize = 18.sp)
                    playlist.thumbnails?.url?.let { img ->
                        AsyncImage(model = img, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun VideoScreen(list: List<Video>, onClick: (String) -> Unit) {
    LazyColumn {
        items(list) { video ->
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onClick(video.videoId) }
            ) {
                Column(Modifier.padding(8.dp).statusBarsPadding()) {
                    Text(video.title, fontSize = 18.sp)
                    video.thumbnail?.let { img -> AsyncImage(model = img, contentDescription = null) }
                }
            }
        }
    }
}

@Composable
fun Loader() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

@Composable
fun VideoPlayer(videoId: String, onClose: () -> Unit) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = { Text("إغلاق", Modifier.clickable { onClose() }) },
        text = {
            AndroidView(
                factory = { context ->
                    YouTubePlayerView(context).apply {
                        lifecycleOwner.lifecycle.addObserver(this)
                        addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                youTubePlayer.loadVideo(videoId, 0f)
                            }
                        })
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }
    )
}
@Composable
fun VideoPlayerScreen(videoId: String, title: String, onBack: () -> Unit) {
    val context = LocalContext.current
    val activity = context as? Activity
    var isFullscreen by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
    ) {
        // زر رجوع — يختفي في fullscreen
        if (!isFullscreen) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
            }
        }

        // مشغل الفيديو
        AndroidView(
            factory = { ctx ->
                YouTubePlayerView(ctx)
            },
            modifier = if (isFullscreen)
                Modifier.fillMaxSize()
            else
                Modifier.fillMaxWidth().height(250.dp)
        )

        // باقي المحتوى — يختفي في fullscreen
        if (!isFullscreen) {
            Text(text = title, color = Color.White, fontSize = 18.sp,
                modifier = Modifier.padding(16.dp))

            Row(Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = {
                    isFullscreen = true
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }) {
                    Text("ملء الشاشة")
                }
                Button(onClick = { }) {
                    Text("PDF")
                }
            }
        }
    }
}