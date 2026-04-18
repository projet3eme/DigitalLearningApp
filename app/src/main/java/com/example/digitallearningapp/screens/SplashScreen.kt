package com.example.digitallearningapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun SplashScreen(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF4A90E2), Color(0xFF2C5282))
                )
            )
            .clickable { onClick() }
    ) {
        // Background Decorative Elements (Blobs)
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .offset(x = (-96).dp, y = (-96).dp)
                    .size(384.dp)
                    .blur(90.dp)
                    .background(Color.White.copy(alpha = 0.1f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = 192.dp)
                    .size(500.dp)
                    .blur(100.dp)
                    .background(Color(0xFF60A5FA).copy(alpha = 0.1f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = 64.dp, y = 128.dp)
                    .size(320.dp)
                    .blur(80.dp)
                    .background(Color(0xFFD4E3FF).copy(alpha = 0.2f), CircleShape)
            )
        }

        // Texture Overlay
        AsyncImage(
            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCvwOO-1cA-Rsml_KzFnX7TtevnkaLWDmtCF-gatvLyyY1oEGX1k-cNJ0JJmxvc2ugNQE9QZxATIvW1t6vXaCBv59xmtfmPCHRrVQMRRY-qj-GfKexsV4obSuAGmKBusjM9Ad-4VymPYDDO8DhMcHV2O_nE6KQJr_kLZb9UspQtRPDqTsW7SpUOGMEwP_UIaodnkv2uhPc-Ml-oSRZLF8fV93H23TqPmbtXZ5K250BTfes8W4yjaTsSnxXm7i0J9PDFUYI_pYjF7PNw",
            contentDescription = null,
            modifier = Modifier.fillMaxSize().alpha(0.1f),
            contentScale = ContentScale.Crop
        )

        // Central Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Book Icon Wrapper
            Box(
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .size(160.dp)
                    .shadow(
                        elevation = 40.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = Color(0xFF1E3A8A).copy(alpha = 0.4f)
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.1f))
                    .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.White
                )
            }

            // Brand Typography
            Text(
                text = "Digital Learning",
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-1.5).sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Arabic Subtitle (RTL)
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Text(
                    text = "منصة تعليمية ذكية",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )
            }

            // Progress Indicator
            Spacer(modifier = Modifier.height(80.dp))
            Box(
                modifier = Modifier
                    .width(192.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.33f)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.8f))
                )
            }
        }

        // Footer Tagline
        Text(
            text = "Premium Academic Experience",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            color = Color.White.copy(alpha = 0.4f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center
        )
    }
}