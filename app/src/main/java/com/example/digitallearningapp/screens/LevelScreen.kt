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

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(Color(0xFF64B5F6))
                .padding(16.dp)
        ) {
            Text(
                text = "مرحبا بك $studentName",
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        levels.forEachIndexed { index, pair ->
            val level = pair.first
            val code = pair.second

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable { onLevelSelected(level) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(90.dp).height(60.dp)
                        .background(
                            color = if (index % 2 == 0) Color(0xFF1A0099) else Color(0xFF00E5FF),
                            shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = code, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Box(
                    modifier = Modifier
                        .weight(1f).height(60.dp)
                        .background(Color.White, RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(text = level, fontSize = 18.sp, color = Color.Black)
                }
            }
            HorizontalDivider(color = Color(0xFFEEEEEE))
        }
    }
}