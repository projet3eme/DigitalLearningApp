package com.example.digitallearningapp.screens

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(
    name: String,
    onNameChange: (String) -> Unit,
    selectedLevel: String,
    onLevelSelected: (String) -> Unit,
    onLoginSuccess: () -> Unit,
    onCreateAccountClick: () -> Unit,
    navController: NavHostController
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val isFormValid = name.isNotBlank() && selectedLevel.isNotBlank()
    val primaryDark = Color(0xFF1A3A6B)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF4A90E2), Color(0xFF2C5282))
                )
            )
    ) {
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
                        imageVector = Icons.AutoMirrored.Filled.List,
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

            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(32.dp)) {
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
                            onValueChange = onNameChange,
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
                                    .shadow(if (isSelected) 8.dp else 4.dp, RoundedCornerShape(16.dp), spotColor = Color.Black.copy(alpha = 0.1f))
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) primaryDark else Color.White)
                                    .clickable { onLevelSelected(level) },
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

                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = {
                            if (isFormValid) {
                                val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                                prefs.edit().putString("student_name", name).apply()
                                prefs.edit().putString("student_level", selectedLevel).apply()
                                onLoginSuccess()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .shadow(20.dp, RoundedCornerShape(999.dp), spotColor = Color.Black.copy(alpha = 0.1f)),
                        interactionSource = interactionSource,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPressed) primaryDark else Color.White,
                            contentColor = if (isPressed) Color.White else primaryDark,
                            disabledContainerColor = Color.White.copy(alpha = 0.5f),
                            disabledContentColor = primaryDark.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(999.dp),
                        enabled = isFormValid
                    ) {
                        Text(
                            text = "تسجيل الدخول",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
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