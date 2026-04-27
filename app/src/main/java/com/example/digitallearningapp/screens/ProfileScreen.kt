package com.example.digitallearningapp.screens

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.digitallearningapp.utils.rememberImagePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    val studentName = prefs.getString("student_name", "الطالب") ?: "الطالب"
    val studentEmail = prefs.getString("user_email", "example@email.com") ?: "example@email.com"

    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    val savedImagePath = prefs.getString("profile_image", null)

    LaunchedEffect(savedImagePath) {
        if (savedImagePath != null) {
            profileImageUri = Uri.parse(savedImagePath)
        }
    }

    val pickImage = rememberImagePicker { uri ->
        profileImageUri = uri
        prefs.edit().putString("profile_image", uri.toString()).apply()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("الملف الشخصي", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "رجوع", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2C5282))
            )
        },
        containerColor = if (isDarkTheme) Color(0xFF121212) else Color(0xFFF0F4FF)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // صورة البروفايل (بدون أيقونة بداخلها)
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable { pickImage() }
                    .background(Brush.verticalGradient(listOf(Color(0xFF4A90E2), Color(0xFF2C5282)))),
                contentAlignment = Alignment.Center
            ) {
                if (profileImageUri != null) {
                    AsyncImage(
                        model = profileImageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = Color.White
                    )
                }
            }

            // أيقونة تعديل الصورة تحت الصورة (خارجها)
            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .clickable { pickImage() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "تعديل الصورة",
                    modifier = Modifier.size(18.dp),
                    tint = if (isDarkTheme) Color(0xFF4A90E2) else Color(0xFF2C5282)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "تغيير الصورة",
                    fontSize = 13.sp,
                    color = if (isDarkTheme) Color(0xFF4A90E2) else Color(0xFF2C5282),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // الاسم تحت الصورة
            Text(
                text = studentName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color.White else Color(0xFF1A3A6B),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // بطاقة المعلومات
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    InfoRow("📧 البريد الإلكتروني", studentEmail, isDarkTheme)
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoRow("📆 تاريخ الانضمام", "2026", isDarkTheme)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // زر الوضع المظلم
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🌙 الوضع المظلم", fontSize = 16.sp, color = if (isDarkTheme) Color.White else Color(0xFF1A3A6B))
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { onThemeChange(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF4A90E2),
                            checkedTrackColor = Color(0xFF4A90E2).copy(alpha = 0.5f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // زر تسجيل الخروج
            Button(
                onClick = {
                    prefs.edit().clear().apply()
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2C5282),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("تسجيل الخروج", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String, isDarkTheme: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = if (isDarkTheme) Color(0xFFAAAAAA) else Color(0xFF888888), fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Medium, color = if (isDarkTheme) Color.White else Color(0xFF1A3A6B), fontSize = 14.sp)
    }
}