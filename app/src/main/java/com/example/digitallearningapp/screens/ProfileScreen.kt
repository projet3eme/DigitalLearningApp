package com.example.digitallearningapp.screens

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.BorderStroke
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
    val prefs = remember { context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE) }

    val studentName = prefs.getString("student_name", "الطالب") ?: "الطالب"
    val studentEmail = prefs.getString("user_email", "example@email.com") ?: "example@email.com"

    // حالة الصورة الشخصية - استرجاع المسار من التفضيلات
    var profileImageUri by remember { 
        mutableStateOf(prefs.getString("profile_image", null)?.let { Uri.parse(it) }) 
    }

    // إعداد أداة اختيار الصور من المعرض
    val pickImage = rememberImagePicker { uri ->
        profileImageUri = uri
        // حفظ المسار الجديد لضمان بقائه متاحاً
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // قسم الصورة الشخصية مع زر تعديل عائم
            Box(
                modifier = Modifier.size(140.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Brush.verticalGradient(listOf(Color(0xFF4A90E2), Color(0xFF2C5282))))
                        .border(4.dp, if (isDarkTheme) Color(0xFF1E1E1E) else Color.White, CircleShape)
                        .clickable { pickImage() }, // الضغط على الصورة يفتح المعرض
                    contentAlignment = Alignment.Center
                ) {
                    if (profileImageUri != null) {
                        AsyncImage(
                            model = profileImageUri,
                            contentDescription = "صورة الملف الشخصي",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = Color.White
                        )
                    }
                }
                
                // زر صغير فوق الصورة لفتح المعرض
                Surface(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .clickable { pickImage() },
                    color = Color(0xFF2C5282),
                    shadowElevation = 6.dp,
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Edit, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // نص "تغيير الصورة" القابل للضغط
            Text(
                text = "تغيير صورة الملف الشخصي",
                fontSize = 14.sp,
                color = if (isDarkTheme) Color(0xFF4A90E2) else Color(0xFF2C5282),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { pickImage() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = studentName,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = if (isDarkTheme) Color.White else Color(0xFF1A3A6B)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // بطاقة المعلومات الشخصية
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ProfileInfoRow("📧 البريد الإلكتروني", studentEmail, isDarkTheme)
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        thickness = 0.5.dp,
                        color = Color.LightGray.copy(alpha = 0.2f)
                    )
                    ProfileInfoRow("📆 تاريخ الانضمام", "2026", isDarkTheme)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // إعدادات المظهر
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "🌙 الوضع المظلم",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDarkTheme) Color.White else Color(0xFF1A3A6B)
                    )
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

            Spacer(modifier = Modifier.height(40.dp))

            // زر تسجيل الخروج - تم تعديله للون الأزرق بناءً على طلبك
            Button(
                onClick = {
                    prefs.edit().clear().apply()
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5282)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("تسجيل الخروج", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String, isDarkTheme: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(
            value,
            fontWeight = FontWeight.Bold,
            color = if (isDarkTheme) Color.White else Color(0xFF1A3A6B),
            fontSize = 14.sp
        )
    }
}
