package com.example.digitallearningapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LevelSelectionScreen(onLevelSelected: (String) -> Unit) {
    val levels = listOf("الابتدائي", "المتوسط", "الثانوي")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            "اختر مستواك الدراسي",
            fontSize = 28.sp,
            modifier = Modifier.padding(16.dp),
            color = Color(0xFF1A237E)
        )
        Spacer(modifier = Modifier.height(24.dp))

        levels.forEach { level ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .clickable { onLevelSelected(level) },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text(text = level, fontSize = 20.sp, color = Color.Black)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearSelectionScreen(level: String, onYearSelected: (String) -> Unit, onBack: () -> Unit) {
    val years = when (level) {
        "الابتدائي" -> listOf("السنة الأولى", "السنة الثانية", "السنة الثالثة", "السنة الرابعة", "السنة الخامسة")
        "المتوسط" -> listOf("السنة الأولى", "السنة الثانية", "السنة الثالثة", "السنة الرابعة")
        else -> listOf("السنة الأولى", "السنة الثانية", "السنة الثالثة")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("اختر السنة الدراسية") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFE3F2FD))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = level,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            }

            items(years) { year ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onYearSelected(year) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                        Text(text = year, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}
