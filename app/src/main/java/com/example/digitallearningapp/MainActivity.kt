package com.example.digitallearningapp

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.digitallearningapp.ui.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DigitalLearningApp()
        }
    }
}

@Composable
fun DigitalLearningApp() {
    var isDarkTheme by remember { mutableStateOf(false) }
    val context = LocalContext.current // إضافة الـ context هنا لحل المشكلة

    MaterialTheme(
        colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme
    ) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val hideBottomBar = currentRoute in listOf(
            "splash", "login_screen", "video_bento/{videoId}", "register", "welcome_screen/{name}"
        )

        Scaffold(
            bottomBar = {
                if (!hideBottomBar) {
                    NavigationBar(
                        containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White,
                        tonalElevation = 8.dp
                    ) {
                        NavigationBarItem(
                            selected = currentRoute?.startsWith("level_screen") == true,
                            onClick = {
                                // استخدام الـ context المعرف أعلاه
                                val prefs = context.getSharedPreferences("user_prefs", MODE_PRIVATE)
                                val name = prefs.getString("student_name", "") ?: ""
                                navController.navigate("level_screen/$name") {
                                    popUpTo(0) { inclusive = false }
                                }
                            },
                            icon = { Icon(Icons.Default.Home, contentDescription = null) },
                            label = { Text("الرئيسية") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF2C5282),
                                selectedTextColor = Color(0xFF2C5282),
                                indicatorColor = Color(0xFF4A90E2).copy(alpha = 0.15f)
                            )
                        )

                        NavigationBarItem(
                            selected = currentRoute == "my_lessons",
                            onClick = {
                                navController.navigate("my_lessons") {
                                    popUpTo(0) { inclusive = false }
                                }
                            },
                            icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null) },
                            label = { Text("دروسي") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF2C5282),
                                selectedTextColor = Color(0xFF2C5282),
                                indicatorColor = Color(0xFF4A90E2).copy(alpha = 0.15f)
                            )
                        )

                        NavigationBarItem(
                            selected = currentRoute == "profile",
                            onClick = {
                                navController.navigate("profile") {
                                    popUpTo(0) { inclusive = false }
                                }
                            },
                            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                            label = { Text("إعدادات") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF2C5282),
                                selectedTextColor = Color(0xFF2C5282),
                                indicatorColor = Color(0xFF4A90E2).copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            AppNavGraph(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                isDarkTheme = isDarkTheme,
                onThemeChange = { isDarkTheme = it }
            )
        }
    }
}

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4A90E2),
    secondary = Color(0xFF2C5282),
    background = Color(0xFFF0F4FF),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1A3A6B),
    onSurface = Color(0xFF1A3A6B)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4A90E2),
    secondary = Color(0xFF2C5282),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)