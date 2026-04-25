package com.example.digitallearningapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LevelSelectionScreen(
    studentName: String,
    onLevelSelected: (String) -> Unit
) {
    val levels = listOf(
        "الابتدائي" to "PRI",
        "المتوسط" to "MID",
        "الثانوي" to "SEC"
    )

    // درجة اللون الأزرق الداكنة السابقة (التي كانت لإطار الترحيب)
    val darkBlue = Color(0xFF2C5282)
    // درجة اللون الأزرق الباردة (الموجودة في 2AP و 4AP)
    val coolBlue = Color(0xFF4A90E2)

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))
    ) {
        // إطار الترحيب باللون الأزرق الداكن
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(darkBlue)
                .padding(16.dp)
        ) {
            Text(
                text = "مرحبا بك $studentName",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        levels.forEachIndexed { index, pair ->
            val level = pair.first
            val code = pair.second

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { onLevelSelected(level) },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(70.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .fillMaxHeight()
                            .background(
                                // جعل MID باللون الداكن، و PRI/SEC باللون البارد
                                color = if (code == "MID") darkBlue else coolBlue
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
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = level,
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
