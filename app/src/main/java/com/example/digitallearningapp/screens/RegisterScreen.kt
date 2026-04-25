package com.example.digitallearningapp.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digitallearningapp.network.SupabaseClient
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onRegisterSuccess: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    var name by remember { mutableStateOf("") }
    var selectedLevel by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val primaryDark = Color(0xFF1A3A6B)
    val isFormValid = name.isNotBlank() && selectedLevel.isNotBlank() && selectedYear.isNotBlank()

    val years = when (selectedLevel) {
        "الابتدائي" -> listOf("السنة الأولى", "السنة الثانية", "السنة الثالثة", "السنة الرابعة", "السنة الخامسة")
        "المتوسط" -> listOf("السنة الأولى", "السنة الثانية", "السنة الثالثة", "السنة الرابعة")
        "الثانوي" -> listOf("السنة الأولى", "السنة الثانية", "السنة الثالثة")
        else -> emptyList()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF4A90E2), Color(0xFF2C5282))
                )
            )
    ) {
        Box(modifier = Modifier.fillMaxSize().alpha(0.1f)) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .offset(x = 200.dp, y = (-50).dp)
                    .background(Color.White, CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-50).dp, y = 50.dp)
                    .background(Color(0xFF93C5FD), CircleShape)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .shadow(20.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(45.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "إنشاء حساب جديد",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "قم بإنشاء حساب للوصول إلى الدروس",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // حقل الاسم
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "الاسم الكامل",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    textAlign = TextAlign.Right
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    if (name.isEmpty()) {
                        Text(
                            text = "أدخل اسمك هنا",
                            color = primaryDark.copy(alpha = 0.4f),
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Right
                        )
                    }
                    BasicTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            color = primaryDark,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Right
                        ),
                        cursorBrush = SolidColor(primaryDark),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // المستوى الدراسي
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "اختر المستوى الدراسي",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    textAlign = TextAlign.Right
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val levels = listOf("الابتدائي", "المتوسط", "الثانوي")
                    levels.forEach { level ->
                        val isSelected = selectedLevel == level
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) primaryDark else Color.White)
                                .clickable {
                                    selectedLevel = level
                                    selectedYear = ""
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = level,
                                color = if (isSelected) Color.White else primaryDark,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // السنة الدراسية
            if (selectedLevel.isNotBlank()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "اختر السنة الدراسية",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 4.dp),
                        textAlign = TextAlign.Right
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(years) { year ->
                            val isSelected = selectedYear == year
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) primaryDark else Color.White)
                                    .clickable { selectedYear = year },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = year,
                                    color = if (isSelected) Color.White else primaryDark,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color(0xFFFF6B6B),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (isFormValid) {
                        isLoading = true
                        coroutineScope.launch {
                            val saved = SupabaseClient.saveStudent(name, selectedLevel, selectedYear)
                            if (saved) {
                                val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                                prefs.edit().putString("student_name", name).apply()
                                prefs.edit().putString("student_level", selectedLevel).apply()
                                prefs.edit().putString("student_year", selectedYear).apply()
                                onRegisterSuccess(name)
                            } else {
                                errorMessage = "حدث خطأ في إنشاء الحساب"
                            }
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                interactionSource = interactionSource,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPressed) primaryDark else Color.White,
                    contentColor = if (isPressed) Color.White else primaryDark,
                    disabledContainerColor = Color.White.copy(alpha = 0.5f),
                    disabledContentColor = primaryDark.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = isFormValid && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = primaryDark,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "إنشاء حساب",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "لديك حساب بالفعل؟ ",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
                Text(
                    text = "تسجيل الدخول",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }
    }
}