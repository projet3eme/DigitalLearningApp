package com.example.digitallearningapp.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.digitallearningapp.model.Video
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

// الألوان السينمائية المتطابقة مع التصميم
val CinematicDark = Color(0xFF1A202C)
val PrimaryBlue = Color(0xFF005DA7)
val SuccessGreen = Color(0xFF006D3C)
val TertiaryOrange = Color(0xFF904900)
val SurfaceVariant = Color(0xFFFFFFFF).copy(alpha = 0.05f)
val OutlineVariant = Color(0xFFFFFFFF).copy(alpha = 0.1f)

@Composable
fun VideoScreen(
    list: List<Video>,
    initialVideoId: String? = null,
    onBack: () -> Unit,
    onClick: (String) -> Unit
) {
    if (list.isEmpty()) {
        Box(Modifier.fillMaxSize().background(CinematicDark), contentAlignment = Alignment.Center) {
            Text("لا توجد فيديوهات متاحة حالياً", color = Color.White)
        }
        return
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    
    // محاولة العثور على الفيديو الممرر في القائمة، أو اختيار الأول
    val initialVideo = list.find { it.videoId == initialVideoId } ?: list.first()
    var selectedVideo by remember { mutableStateOf(initialVideo) }

    // تحديث الفيديو المختار إذا تغير initialVideoId خارجياً
    LaunchedEffect(initialVideoId) {
        list.find { it.videoId == initialVideoId }?.let {
            selectedVideo = it
        }
    }

    Scaffold(
        containerColor = CinematicDark
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            AmbientGlowDecoration()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // مشغل الفيديو الاحترافي
                selectedVideo.let { video ->
                    key(video.videoId) {
                        VideoPlayerSection(video.videoId, lifecycleOwner)
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        selectedVideo.let { video ->
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = video.title,
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = "جاري التشغيل الآن",
                                    color = Color(0xFFA4C9FF),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    item { ActionButtonsSection() }

                    item { LessonDescriptionSection() }

                    item {
                        Text(
                            text = "بقية الدروس في هذه القائمة",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    items(list) { video ->
                        VideoListItem(
                            video = video,
                            isSelected = video.videoId == selectedVideo.videoId,
                            onSelect = {
                                selectedVideo = video
                                onClick(video.videoId)
                            }
                        )
                    }
                }
            }

            CustomTopBar(selectedVideo.title, onBack)
        }
    }
}

@Composable
fun VideoPlayerSection(videoId: String, lifecycleOwner: androidx.lifecycle.LifecycleOwner) {
    // استخراج الـ ID بشكل آمن
    val cleanId = remember(videoId) {
        when {
            videoId.contains("v=") -> videoId.substringAfter("v=").substringBefore("&")
            videoId.contains("youtu.be/") -> videoId.substringAfter("youtu.be/").substringBefore("?")
            else -> videoId
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .background(Color.Black)
            .shadow(20.dp)
    ) {
        AndroidView(
            factory = { context ->
                YouTubePlayerView(context).apply {
                    enableAutomaticInitialization = false
                    lifecycleOwner.lifecycle.addObserver(this)

                    val listener = object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            if (cleanId.isNotEmpty()) {
                                youTubePlayer.loadVideo(cleanId, 0f)
                            }
                        }
                    }
                    val options = IFramePlayerOptions.Builder()
                        .controls(1)
                        .fullscreen(1)
                        .build()
                    initialize(listener, options)
                }
            },
            modifier = Modifier.fillMaxSize(),
            onRelease = { view ->
                lifecycleOwner.lifecycle.removeObserver(view)
            }
        )
    }
}

@Composable
fun VideoListItem(video: Video, isSelected: Boolean, onSelect: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.White.copy(alpha = 0.15f) else SurfaceVariant
        ),
        border = if (isSelected) BorderStroke(1.dp, PrimaryBlue) else null
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(100.dp, 60.dp).clip(RoundedCornerShape(8.dp))) {
                AsyncImage(model = video.thumbnail, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                if (isSelected) {
                    Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = video.title, color = Color.White, fontSize = 15.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium, maxLines = 2, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ActionButtonsSection() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(onClick = { }, modifier = Modifier.weight(1f).height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen), shape = RoundedCornerShape(12.dp)) {
            Icon(Icons.Default.Fullscreen, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("ملء الشاشة", fontWeight = FontWeight.Bold)
        }
        Button(onClick = { }, modifier = Modifier.weight(1f).height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = TertiaryOrange), shape = RoundedCornerShape(12.dp)) {
            Icon(Icons.Default.Description, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("تحميل PDF", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LessonDescriptionSection() {
    Column(modifier = Modifier.fillMaxWidth().background(SurfaceVariant, RoundedCornerShape(16.dp)).border(1.dp, OutlineVariant, RoundedCornerShape(16.dp)).padding(20.dp)) {
        Text("وصف الدرس", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        Text("في هذا الدرس، سنقوم بشرح المفاهيم الأساسية للتفاضل والتكامل وكيفية تطبيقها في المسائل الرياضية المعقدة بأسلوب مبسط.", color = Color.White.copy(alpha = 0.7f), fontSize = 15.sp, lineHeight = 24.sp, textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun CustomTopBar(title: String, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Brush.verticalGradient(listOf(CinematicDark, Color.Transparent))).statusBarsPadding().padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.1f), CircleShape)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1, modifier = Modifier.widthIn(max = 200.dp))
                    Text("الأستاذ", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun AmbientGlowDecoration() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .offset(x = 100.dp, y = (-50).dp)
                .size(300.dp)
                .blur(100.dp)
                .background(PrimaryBlue.copy(alpha = 0.15f), CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-50).dp, y = 50.dp)
                .size(300.dp)
                .blur(100.dp)
                .background(SuccessGreen.copy(alpha = 0.1f), CircleShape)
        )
    }
}
