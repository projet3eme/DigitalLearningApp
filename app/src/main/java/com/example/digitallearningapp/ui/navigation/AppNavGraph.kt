package com.example.digitallearningapp.ui.navigation

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.digitallearningapp.screens.*
import com.example.digitallearningapp.utils.FirstLaunchManager
import com.example.digitallearningapp.viewmodel.PlaylistViewModel
import com.example.digitallearningapp.viewmodel.SubjectViewModel
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false,
    onThemeChange: (Boolean) -> Unit = {},
    isFirstLaunch: Boolean = true
) {
    val playlistViewModel: PlaylistViewModel = viewModel()
    val apiKey = "AIzaSyC3VzbxUXNJHp_B3xjuSFUpjr3FzWFLSBg"
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val firstLaunchManager = remember { FirstLaunchManager(context) }

    val savedName = prefs.getString("student_name", "") ?: ""
    val startDestination = if (savedName.isNotEmpty()) "level_screen/$savedName" else "splash"

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("splash") {
            SplashScreen(
                onClick = {
                    navController.navigate("login_screen") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                firstLaunchManager = firstLaunchManager
            )
        }

        composable("login_screen") {
            LoginScreen(
                navController = navController,
                onLoginSuccess = {
                    val studentName = prefs.getString("student_name", "") ?: ""
                    navController.navigate("level_screen/$studentName") {
                        popUpTo("login_screen") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { studentName ->
                    prefs.edit().putString("student_name", studentName).apply()
                    navController.navigate("welcome_screen/$studentName") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate("login_screen") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "welcome_screen/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val studentName = backStackEntry.arguments?.getString("name") ?: ""
            WelcomeScreen(
                studentName = studentName,
                onTimeout = {
                    navController.navigate("level_screen/$studentName") {
                        popUpTo("welcome_screen/$studentName") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "level_screen/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            LevelSelectionScreen(
                studentName = name,
                onLevelSelected = { level ->
                    navController.navigate("years_screen/$level/$name")
                }
            )
        }

        composable(
            route = "years_screen/{level}/{name}",
            arguments = listOf(
                navArgument("level") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val level = backStackEntry.arguments?.getString("level") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""
            YearSelectionScreen(
                level = level,
                onYearSelected = { year ->
                    navController.navigate("subjects_screen/$level/$year/$name")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "subjects_screen/{level}/{year}/{name}",
            arguments = listOf(
                navArgument("level") { type = NavType.StringType },
                navArgument("year") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val levelArg = backStackEntry.arguments?.getString("level") ?: ""
            val yearArg  = backStackEntry.arguments?.getString("year") ?: ""
            val name     = backStackEntry.arguments?.getString("name") ?: ""
            val subjectViewModel: SubjectViewModel = viewModel()

            LaunchedEffect(levelArg, yearArg) {
                val cleanLevel = when {
                    levelArg.contains("ابتد") -> "ابتدائي"
                    levelArg.contains("متوسط") -> "متوسط"
                    levelArg.contains("ثانو") -> "ثانوي"
                    else -> levelArg
                }
                val cleanYear = when {
                    yearArg.contains("الأولى") -> "الأولى"
                    yearArg.contains("الثانية") -> "الثانية"
                    yearArg.contains("الثالثة") -> "الثالثة"
                    yearArg.contains("الرابعة") -> "الرابعة"
                    yearArg.contains("الخامسة") -> "الخامسة"
                    else -> yearArg
                }
                subjectViewModel.fetchSubjects(cleanLevel, cleanYear)
            }

            SubjectScreen(
                level = levelArg,
                year = yearArg,
                subjects = subjectViewModel.subjects.value,
                isLoading = subjectViewModel.isLoading.value,
                onBack = { navController.popBackStack() },
                onSubjectClick = { _, _, _, playlistId ->
                    val encoded = URLEncoder.encode(playlistId, "UTF-8")
                    navController.navigate("playlist_screen/$encoded/$name")
                }
            )
        }

        composable(
            route = "playlist_screen/{playlistId}/{name}",
            arguments = listOf(
                navArgument("playlistId") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val playlistId = URLDecoder.decode(
                backStackEntry.arguments?.getString("playlistId") ?: "", "UTF-8"
            )
            val name = backStackEntry.arguments?.getString("name") ?: ""

            LaunchedEffect(playlistId) {
                if (playlistId.isNotEmpty()) {
                    playlistViewModel.fetchVideos(playlistId, apiKey)
                }
            }

            PlaylistScreen(
                studentName = name,
                channelId = playlistId,
                videos = playlistViewModel.videos.value,
                isLoading = playlistViewModel.isLoading.value,
                errorMessage = playlistViewModel.errorMessage.value,
                onBackClick = { navController.popBackStack() },
                onProfileClick = { navController.navigate("profile") },
                onVideoClick = { videoId ->
                    val encodedId = URLEncoder.encode(videoId, "UTF-8")
                    navController.navigate("video_web/$encodedId")
                },
                onRetryClick = { id -> playlistViewModel.fetchVideos(id, apiKey) }
            )
        }

        composable(
            route = "video_web/{videoId}",
            arguments = listOf(navArgument("videoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val videoId = URLDecoder.decode(
                backStackEntry.arguments?.getString("videoId") ?: "", "UTF-8"
            )
            VideoScreen(
                list = playlistViewModel.videos.value,
                initialVideoId = videoId,
                onBack = { navController.popBackStack() },
                onClick = { }
            )
        }

        composable("my_lessons") {
            MyLessonsScreen(
                onBack = { navController.popBackStack() },
                onVideoClick = { videoId, _ ->
                    val encodedId = URLEncoder.encode(videoId, "UTF-8")
                    navController.navigate("video_web/$encodedId")
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    prefs.edit().clear().apply()
                    navController.navigate("login_screen") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange
            )
        }
    }
}
