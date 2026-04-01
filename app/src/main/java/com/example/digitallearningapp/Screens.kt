
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
                Column(Modifier.padding(8.dp)) {
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
                Column(Modifier.padding(8.dp)) {
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
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = { Text("إغلاق", Modifier.clickable { onClose() }) },
        text = {
            AndroidView(factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    loadUrl("https://www.youtube.com/embed/$videoId")
                }
            })
        }
    )
}