package com.example.digitallearningapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.foundation.Canvas

@Composable
fun LoginScreen(
    name: String,
    onNameChange: (String) -> Unit,
    selectedLevel: String,
    onLevelSelected: (String) -> Unit,
    onLoginSuccess: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    // التحقق من صحة البيانات
    val isFormValid = name.isNotBlank() && selectedLevel.isNotBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF4A90E2), Color(0xFF2C5282))
                )
            )
    ) {
        // Decorative background texture
        Box(modifier = Modifier.fillMaxSize().alpha(0.2f)) {
            Box(
                modifier = Modifier
                    .size(400.dp)
                    .offset(x = 200.dp, y = (-50).dp)
                    .blur(120.dp)
                    .background(Color.White, CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-50).dp, y = 50.dp)
                    .blur(100.dp)
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
            // App Branding
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .shadow(20.dp, RoundedCornerShape(24.dp), spotColor = Color(0xFF1E3A8A).copy(alpha = 0.2f))
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(60.dp)
                    )
                }
                Text(
                    text = "Digital Learning",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = (-1).sp
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Form Section
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(32.dp)) {
                // Name Input
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
                            .background(Color.White.copy(alpha = 0.1f))
                            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 24.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        if (name.isEmpty()) {
                            Text(
                                text = "أدخل اسمك هنا",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 16.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Right
                            )
                        }
                        BasicTextField(
                            value = name,
                            onValueChange = onNameChange,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Right
                            ),
                            cursorBrush = SolidColor(Color.White),
                            singleLine = true
                        )
                    }
                }

                // Level Selector
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
                                    .shadow(if (isSelected) 8.dp else 0.dp, RoundedCornerShape(16.dp), spotColor = Color(0xFF064E3B).copy(alpha = 0.2f))
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) Color(0xFF1A3A6B) else Color.White.copy(alpha = 0.1f))
                                    .clickable { onLevelSelected(level) }
                                    .then(
                                        if (!isSelected) Modifier.border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                                        else Modifier
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = level,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Login Button
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = {
                            if (isFormValid) {
                                onLoginSuccess()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .shadow(20.dp, RoundedCornerShape(999.dp), spotColor = Color.Black.copy(alpha = 0.1f)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFormValid) Color(0xFF1A3A6B) else Color.Gray.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Text(
                            text = "تسجيل الدخول",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ليس لديك حساب؟ ",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "إنشاء حساب جديد",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { onCreateAccountClick() }.padding(horizontal = 4.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Bottom curve decoration
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .align(Alignment.BottomCenter)
                .alpha(0.05f)
        ) {
            drawBottomCurve()
        }
    }
}

private fun DrawScope.drawBottomCurve() {
    val width = size.width
    val height = size.height
    val path = Path().apply {
        moveTo(0f, height * 0.8f)
        cubicTo(
            width * 0.15f, height * 0.95f,
            width * 0.25f, height * 0.8f,
            width * 0.3f, height * 0.6f
        )
        cubicTo(
            width * 0.4f, height * 0.2f,
            width * 0.5f, height * 0.1f,
            width * 0.6f, height * 0.3f
        )
        cubicTo(
            width * 0.8f, height * 0.6f,
            width * 0.9f, height * 0.85f,
            width, height * 0.8f
        )
        lineTo(width, height)
        lineTo(0f, height)
        close()
    }
    drawPath(path = path, color = Color.White)
}
