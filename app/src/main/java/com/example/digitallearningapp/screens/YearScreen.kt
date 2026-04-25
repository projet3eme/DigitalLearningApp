package com.example.digitallearningapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun YearSelectionScreen(
    level: String,
    onYearSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    val years = when (level) {
        "الابتدائي" -> listOf(
            "السنة الأولى" to "1AP",
            "السنة الثانية" to "2AP",
            "السنة الثالثة" to "3AP",
            "السنة الرابعة" to "4AP",
            "السنة الخامسة" to "5AP"
        )
        "المتوسط" -> listOf(
            "السنة الأولى" to "1AM",
            "السنة الثانية" to "2AM",
            "السنة الثالثة" to "3AM",
            "السنة الرابعة" to "4AM"
        )
        else -> listOf(
            "السنة الأولى" to "1AS",
            "السنة الثانية" to "2AS",
            "السنة الثالثة" to "3AS"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FF)) // خلفية أفتح قليلاً لبروز البطاقات
    ) {
        // Header بتدرج أزرق متناسق
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF4A90E2), Color(0xFF2C5282))
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = level,
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        years.forEachIndexed { index, pair ->
            val year = pair.first
            val code = pair.second

            // استخدام البطاقات (Cards) لجعل الأطر بارزة
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { onYearSelected(year) },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(70.dp)
                ) {
                    // صندوق الكود الملون (أزرق بارد أو داكن)
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .fillMaxHeight()
                            .background(
                                color = if (index % 2 == 0) Color(0xFF4A90E2) else Color(0xFF2C5282)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = code,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                    // اسم السنة
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = year,
                            fontSize = 18.sp,
                            color = Color(0xFF1A3A6B),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
