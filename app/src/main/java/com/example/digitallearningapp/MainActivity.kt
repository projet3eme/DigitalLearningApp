package com.example.digitallearningapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.digitallearningapp.ui.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val hideBottomBar = currentRoute in listOf(
                    "splash",
                    "login_screen",
                    "video_bento/{videoId}"
                )

                Scaffold(
                    bottomBar = {
                        if (!hideBottomBar) {
                            NavigationBar(
                                containerColor = Color.White,
                                tonalElevation = 8.dp
                            ) {
                                // 1. الرئيسية
                                NavigationBarItem(
                                    selected = currentRoute?.startsWith("level_screen") == true,
                                    onClick = {
                                        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
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

                                // 2. دروسي
                                NavigationBarItem(
                                    selected = currentRoute?.startsWith("playlist_screen") == true,
                                    onClick = {
                                        // يرجع لآخر playlist أو للرئيسية
                                        if (navController.previousBackStackEntry != null) {
                                            navController.navigate("level_screen/${
                                                getSharedPreferences("user_prefs", MODE_PRIVATE)
                                                    .getString("student_name", "") ?: ""
                                            }")
                                        }
                                    },
                                    icon = { Icon(Icons.Default.MenuBook, contentDescription = null) },
                                    label = { Text("دروسي") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFF2C5282),
                                        selectedTextColor = Color(0xFF2C5282),
                                        indicatorColor = Color(0xFF4A90E2).copy(alpha = 0.15f)
                                    )
                                )

                                // 3. الإعدادات
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
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}