package com.example.digitallearningapp.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import com.example.digitallearningapp.model.Video
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun VideoScreen(
    list: List<Video>,
    initialVideoId: String? = null,
    onBack: () -> Unit,
    onClick: (String) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val lifecycleOwner = LocalLifecycleOwner.current

    val safeList = remember(list, initialVideoId) {
        if (list.isEmpty() && initialVideoId != null) {
            listOf(Video("جاري التحميل...", initialVideoId, null))
        } else list
    }

    var selectedVideo by remember {
        mutableStateOf(safeList.find { it.videoId == initialVideoId } ?: safeList.firstOrNull())
    }
    var isFullscreen by remember { mutableStateOf(false) }

    BackHandler(enabled = isFullscreen) {
        isFullscreen = false
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    Scaffold(containerColor = Color(0xFF0F172A)) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            selectedVideo?.let { video ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(if (isFullscreen) Modifier.fillMaxSize() else Modifier.aspectRatio(16f / 9f))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    key(video.videoId) {
                        ProfessionalTrustedPlayer(
                            videoId = video.videoId.trim(),
                            lifecycleOwner = lifecycleOwner,
                            onFullscreenChange = { full ->
                                isFullscreen = full
                                activity?.requestedOrientation = if (full)
                                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                                else
                                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            }
                        )
                    }
                }
            }

            if (!isFullscreen) {
                // زر ملء الشاشة أسفل الفيديو مباشرة
                Button(
                    onClick = {
                        isFullscreen = true
                        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Fullscreen, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("مشاهدة بملء الشاشة", color = Color.White, fontWeight = FontWeight.Bold)
                }

                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
                    item {
                        Text(
                            text = selectedVideo?.title ?: "",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Right,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp)
                        )
                    }

                    items(safeList) { item ->
                        LessonRow(
                            video = item,
                            isSelected = item.videoId == selectedVideo?.videoId
                        ) {
                            selectedVideo = item
                            onClick(item.videoId)
                        }
                    }
                }
            }
        }

        if (!isFullscreen) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(16.dp)
                    .statusBarsPadding()
                    .background(Color.White.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
        }
    }
}
@Composable
fun SupabaseVideoPlayer(videoUrl: String) {
    val context = LocalContext.current
    val exoPlayer = remember(videoUrl) {
        androidx.media3.exoplayer.ExoPlayer.Builder(context).build().apply {
            setMediaItem(androidx.media3.common.MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(videoUrl) {
        onDispose { exoPlayer.release() }
    }

    AndroidView(
        factory = {
            androidx.media3.ui.PlayerView(it).apply {
                player = exoPlayer
                useController = true
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
@Composable
fun ProfessionalTrustedPlayer(
    videoId: String,
    lifecycleOwner: LifecycleOwner,
    onFullscreenChange: (Boolean) -> Unit
) {
    if (videoId.startsWith("https://")) {
        SupabaseVideoPlayer(videoUrl = videoId)
        return
    }
    val context = LocalContext.current


    val isSupabaseVideo = videoId.startsWith("http")

    if (isSupabaseVideo) {

        val exoPlayer = remember(videoId) {
            androidx.media3.exoplayer.ExoPlayer.Builder(context).build().apply {
                setMediaItem(androidx.media3.common.MediaItem.fromUri(videoId))
                prepare()
                playWhenReady = true
            }
        }

        DisposableEffect(videoId) {
            onDispose { exoPlayer.release() }
        }

        AndroidView(
            factory = {
                androidx.media3.ui.PlayerView(it).apply {
                    player = exoPlayer
                    useController = true
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    } else {

        AndroidView(
            factory = { ctx ->
                YouTubePlayerView(ctx).apply {
                    enableAutomaticInitialization = false
                    lifecycleOwner.lifecycle.addObserver(this)
                    val options = IFramePlayerOptions.Builder()
                        .controls(1)
                        .fullscreen(0)
                        .rel(0)
                        .ivLoadPolicy(3)
                        .ccLoadPolicy(0)
                        .origin("https://www.youtube-nocookie.com")
                        .build()
                    initialize(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.cueVideo(videoId, 0f)
                            postDelayed({ youTubePlayer.play() }, 500)
                        }
                    }, options)
                }
            },
            modifier = Modifier.fillMaxSize(),
            onRelease = { view ->
                view.release()
                lifecycleOwner.lifecycle.removeObserver(view)
            }
        )
    }
}
@Composable
fun LessonRow(video: Video, isSelected: Boolean, onSelect: () -> Unit) {
    Surface(
        onClick = onSelect,
        color = if (isSelected) Color(0xFF3B82F6).copy(alpha = 0.1f) else Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) BorderStroke(1.dp, Color(0xFF3B82F6)) else null,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = video.title,
                color = if (isSelected) Color(0xFF3B82F6) else Color.White,
                fontSize = 14.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp),
                textAlign = TextAlign.Right,
                maxLines = 2
            )
            Box(Modifier
                .size(90.dp, 55.dp)
                .clip(RoundedCornerShape(8.dp))) {
                AsyncImage(
                    model = video.thumbnail,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                if (isSelected) {
                    Box(Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.4f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.PlayArrow, null, tint = Color.White)
                    }
                }
            }
        }
    }

}
