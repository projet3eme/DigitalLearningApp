package com.example.digitallearningapp.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLessonsScreen(
    onBack: () -> Unit,
    onVideoClick: (String, String) -> Unit
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val studentName = prefs.getString("student_name", "الطالب") ?: "الطالب"
    val lastVideoId = prefs.getString("last_watched_video_id", null)
    val lastVideoTitle = prefs.getString("last_watched_video_title", "لا يوجد")
    val lastTimestamp = prefs.getLong("last_watched_timestamp", 0)
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C5282)
                )
            )
        },
        containerColor = Color(0xFFF0F4FF)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // قسم الإحصائيات
            item {
                Text(
                    text = "إنجازاتك",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A3A6B),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // بطاقة 1
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.TrendingUp, null, tint = Color(0xFF4A90E2), modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("0", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A3A6B))
                            Text("دروس مكتملة", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    // بطاقة 2
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.History, null, tint = Color(0xFF4A90E2), modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("0", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A3A6B))
                            Text("ساعات التعلم", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    // بطاقة 3
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.PlayArrow, null, tint = Color(0xFF4A90E2), modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("0", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A3A6B))
                            Text("مواد حالية", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }

            // عنوان آخر الدروس
            item {
                Text(
                    text = "آخر الدروس المشاهدة",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A3A6B),
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }

            // عنصر عند عدم وجود دروس
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "لا توجد دروس مشاهد بعد",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // عنوان الدروس المقترحة
            item {
                Text(
                    text = "دروس مقترحة لك",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A3A6B),
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }

            // عنصر عند عدم وجود دروس مقترحة
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "لا توجد دروس مقترحة حالياً",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}