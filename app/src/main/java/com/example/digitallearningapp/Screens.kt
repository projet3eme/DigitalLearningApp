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
import androidx.compose.foundation.shape.RoundedCornerShape

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
fun LevelScreen  (onClick: (String, String) -> Unit) {

    val levels = listOf("الابتدائي", "المتوسط", "الثانوي")
    var expandedLevel by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC8E6C9))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // 🔥 هذا هو السر
    )
    {

        // 🔹 العنوان
        Text(
            text = "اختر مستواك التعليمي",
            fontSize = 45.sp,
            color = Color(0xFF121212),
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // مهم للتوازن
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {

            items(levels) { level ->

                Column {

                    // 🟡 كرت المستوى
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .clickable {
                                expandedLevel =
                                    if (expandedLevel == level) null else level
                            },
                        shape = RoundedCornerShape(25.dp), // دائري
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFFDE7)
                                    // أصفر فاتح
                        ),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(20.dp)
                                .wrapContentWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text("📚", fontSize = 26.sp)

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = level,
                                fontSize = 22.sp,
                                color = Color.Black
                            )
                        }
                    }

                    // 🟢 السنوات
                    if (expandedLevel == level) {

                        val years = when (level) {
                            "الابتدائي" -> listOf(
                                "السنة الأولى",
                                "السنة الثانية",
                                "السنة الثالثة",
                                "السنة الرابعة",
                                "السنة الخامسة"
                            )
                            "المتوسط" -> listOf(
                                "السنة الأولى",
                                "السنة الثانية",
                                "السنة الثالثة",
                                "السنة الرابعة"
                            )
                            "الثانوي" -> listOf(
                                "السنة الأولى",
                                "السنة الثانية",
                                "السنة الثالثة"
                            )
                            else -> emptyList()
                        }

                        years.forEach { year ->

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, top = 8.dp)
                                    .clickable {
                                        onClick(level, year)
                                    },
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Text(
                                    text = year,
                                    modifier = Modifier.padding(14.dp),
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun SubjectScreen(onClick: (String) -> Unit) {

    val subjects = listOf("رياضيات", "لغة عربية", "تربية اسلامية")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("اختر المادة")

        Spacer(Modifier.height(20.dp))

        subjects.forEach { subject ->
            Button(onClick = { onClick(subject) }) {
                Text(subject)
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}


@Composable
fun PlaylistScreen(list: List<Playlist>, onClick: (String) -> Unit) {

    LazyColumn {

        items(list) { playlist ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        // 🔥 هذا هو المهم
                        onClick(playlist.id)
                    }
            ) {

                Column(Modifier.padding(12.dp)) {

                    Text(
                        text = playlist.title,
                        fontSize = 18.sp
                    )

                    Spacer(Modifier.height(8.dp))

                    playlist.thumbnails?.url?.let { img ->
                        AsyncImage(
                            model = img,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )
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
                    video.thumbnail?.let { img ->
                        AsyncImage(model = img, contentDescription = null)
                    }
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
        if (!isFullscreen) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
            }
        }

        AndroidView(
            factory = { ctx ->
                YouTubePlayerView(ctx)
            },
            modifier = if (isFullscreen)
                Modifier.fillMaxSize()
            else
                Modifier.fillMaxWidth().height(250.dp)
        )

        if (!isFullscreen) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(16.dp)
            )

            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
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