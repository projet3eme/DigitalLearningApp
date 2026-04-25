package com.example.digitallearningapp.screens

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
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
    val studentLevel = prefs.getString("student_level", "ابتدائي") ?: "ابتدائي"
    val studentYear = prefs.getString("student_year", "السنة الأولى") ?: "السنة الأولى"

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
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // الصورة والاسم
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
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
                        Icon(Icons.Default.Person, null, modifier = Modifier.size(50.dp), tint = Color.White)
                    }
                }
                Box(
                    modifier = Modifier
                        .offset(y = (-12).dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4A90E2))
                        .border(2.dp, Color.White, CircleShape)
                        .clickable { pickImage() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Edit, "تعديل", modifier = Modifier.size(14.dp), tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = studentName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color.White else Color(0xFF1A3A6B)
            )
            Text(
                text = "$studentLevel - $studentYear",
                fontSize = 14.sp,
                color = Color(0xFF4A90E2)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // بطاقة المعلومات
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    InfoRow("الاسم", studentName, isDarkTheme)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = if (isDarkTheme) Color(0xFF333333) else Color(0xFFEEEEEE))
                    InfoRow("المستوى", studentLevel, isDarkTheme)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = if (isDarkTheme) Color(0xFF333333) else Color(0xFFEEEEEE))
                    InfoRow("السنة", studentYear, isDarkTheme)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // زر الوضع المظلم
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White),
                shape = RoundedCornerShape(12.dp)
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

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ زر تسجيل الخروج - لون أزرق متناسق مع الواجهة
            Button(
                onClick = {
                    prefs.edit().clear().apply()
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2C5282), // نفس لون الـ TopAppBar
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Logout, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("تسجيل الخروج", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
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