package com.example.digitallearningapp.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.digitallearningapp.model.Video

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    studentName: String,
    channelId: String,
    videos: List<Video>,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onVideoClick: (String) -> Unit,
    onRetryClick: (String) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            PlaylistTopBar(studentName, onBackClick, onProfileClick)
        },
        containerColor = Color(0xFFF9F9FF)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF2C5282)
                    )
                }
                errorMessage != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Warning, null, tint = Color.Red, modifier = Modifier.size(64.dp))
                        Spacer(Modifier.height(16.dp))
                        Text(errorMessage, textAlign = TextAlign.Center, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(24.dp))
                        Button(onClick = { onRetryClick(channelId) }) { Text("إعادة المحاولة") }
                    }
                }
                videos.isEmpty() -> {
                    Text("لا توجد دروس متاحة حالياً", Modifier.align(Alignment.Center), color = Color.Gray)
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        item {
                            HeaderSection(studentName)
                        }

                        items(videos) { video ->
                            Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
                                LessonCard(
                                    video = video,
                                    onVideoClick = { onVideoClick(video.videoId) },
                                    onPdfClick = {
                                        // Placeholder URL for Google Drive PDF
                                        val pdfUrl = "https://www.google.com" 
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
                                        context.startActivity(intent)
                                    }
                                )
                            }
                        }

                        item {
                            Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
                                HintBox()
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistTopBar(name: String, onBack: () -> Unit, onProfile: () -> Unit) {
    val gradient = Brush.verticalGradient(colors = listOf(Color(0xFF4A90E2), Color(0xFF2C5282)))
    Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 4.dp) {
        Box(modifier = Modifier.fillMaxWidth().background(gradient).statusBarsPadding()) {
            TopAppBar(
                title = { Text("مرحباً، $name", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = onProfile) {
                        Icon(Icons.Default.Person, "Profile", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    }
}

@Composable
fun HeaderSection(name: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 32.dp)) {
        Text("🎬 دروسك المتاحة", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF161C27))
        Spacer(Modifier.height(8.dp))
        Text("استمر في التعلم يا $name! الطريق للتفوق يبدأ بدرس.", fontSize = 16.sp, color = Color(0xFF2C5282).copy(alpha = 0.7f))
    }
}

@Composable
fun LessonCard(video: Video, onVideoClick: () -> Unit, onPdfClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Video Thumbnail Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clickable { onVideoClick() }
            ) {
                AsyncImage(
                    model = video.thumbnail,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier.align(Alignment.Center).size(56.dp).background(Color.Black.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(32.dp))
                }
            }

            // Info and PDF Section
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = video.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF161C27), maxLines = 2)
                
                Spacer(Modifier.height(12.dp))

                // PDF Download Button
                OutlinedButton(
                    onClick = onPdfClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF2C5282).copy(alpha = 0.2f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2C5282))
                ) {
                    Icon(Icons.Default.Description, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("تحميل الملخص PDF", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun HintBox() {
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color(0xFF2976C7).copy(alpha = 0.08f)).padding(24.dp)) {
        Column {
            Text("نصيحة اليوم 💡", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF005DA7))
            Spacer(modifier = Modifier.height(8.dp))
            Text("النجاح هو مجموع خطوات صغيرة تتكرر يوماً بعد يوم.", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF161C27))
        }
    }
}
