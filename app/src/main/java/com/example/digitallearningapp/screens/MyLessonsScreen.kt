package com.example.digitallearningapp.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.digitallearningapp.model.Video
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLessonsScreen(
    onBack: () -> Unit,
    onVideoClick: (String, String) -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE) }
    val studentName = prefs.getString("student_name", "الطالب") ?: "الطالب"
    
    // استرجاع الدروس المشاهدة من الذاكرة
    val watchedVideos = remember {
        val json = Json { ignoreUnknownKeys = true }
        val watchedJson = prefs.getString("watched_videos", "[]") ?: "[]"
        try {
            json.decodeFromString<List<Video>>(watchedJson)
        } catch (e: Exception) {
            emptyList<Video>()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("دروسي", fontWeight = FontWeight.Bold, color = Color.White)
                        Text(
                            text = "مرحباً $studentName",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2C5282))
            )
        },
        containerColor = Color(0xFFF0F4FF)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
        ) {
            // قسم الإحصائيات (تعديل القيم لتكون حقيقية)
            item {
                Text(
                    text = "إنجازاتك",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A3A6B),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.TrendingUp,
                        value = "${watchedVideos.size}",
                        label = "دروس شاهدتها"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.History,
                        value = "${watchedVideos.size * 10}د", // مثال تقريبي للوقت
                        label = "وقت التعلم"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.PlayArrow,
                        value = if(watchedVideos.isNotEmpty()) "نشط" else "0",
                        label = "الحالة"
                    )
                }
            }

            // عنوان الدروس المشاهدة مؤخراً
            item {
                Text(
                    text = "آخر الدروس المشاهدة",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A3A6B),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (watchedVideos.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("لم تشاهد أي دروس بعد. ابدأ رحلتك الآن!", color = Color.Gray, textAlign = TextAlign.Center)
                        }
                    }
                }
            } else {
                items(watchedVideos) { video ->
                    WatchedVideoItem(
                        video = video,
                        onClick = { onVideoClick(video.videoId, video.title) }
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(modifier: Modifier, icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = Color(0xFF4A90E2), modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A3A6B))
            Text(label, fontSize = 10.sp, color = Color.Gray, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun WatchedVideoItem(video: Video, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Column(
                modifier = Modifier.weight(1f).padding(end = 12.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = video.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A3A6B),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Right
                )
                Text(text = "اضغط للمتابعة", fontSize = 11.sp, color = Color(0xFF4A90E2))
            }
            
            AsyncImage(
                model = video.thumbnail,
                contentDescription = null,
                modifier = Modifier.size(width = 90.dp, height = 55.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}
