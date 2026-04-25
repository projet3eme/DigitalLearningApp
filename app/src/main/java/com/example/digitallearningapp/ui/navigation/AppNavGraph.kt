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
import com.example.digitallearningapp.viewmodel.PlaylistViewModel
import com.example.digitallearningapp.viewmodel.SubjectViewModel

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    val viewModel: PlaylistViewModel = viewModel()
    val apiKey = "AIzaSyC3VzbxUXNJHp_B3xjuSFUpjr3FzWFLSBg"
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // تحقق إذا الطالب سجل دخول من قبل
    val savedName = prefs.getString("student_name", "") ?: ""
    val startDest = if (savedName.isNotEmpty()) "level_screen/$savedName" else "splash"

    NavHost(
        navController = navController,
        startDestination = startDest,
        modifier = modifier
    ) {
        composable("splash") {
            SplashScreen(onClick = { navController.navigate("login_screen") })
        }

        composable("login_screen") {
            var name by remember { mutableStateOf("") }
            var selectedLevel by remember { mutableStateOf("") }

            LoginScreen(
                name = name,
                onNameChange = { name = it },
                selectedLevel = selectedLevel,
                onLevelSelected = { selectedLevel = it },
                onLoginSuccess = {
                    // احفظ الاسم
                    prefs.edit().putString("student_name", name).apply()
                    navController.navigate("level_screen/$name") {
                        popUpTo("login_screen") { inclusive = true }
                    }
                },
                onCreateAccountClick = {}
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
                    levelArg.contains("ثانوي") -> "ثانوي"
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
                    val encoded = java.net.URLEncoder.encode(playlistId, "UTF-8")
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
            val playlistId = java.net.URLDecoder.decode(
                backStackEntry.arguments?.getString("playlistId") ?: "", "UTF-8"
            )
            val name       = backStackEntry.arguments?.getString("name") ?: ""

            LaunchedEffect(playlistId) {
                if (playlistId.isNotEmpty()) {
                    viewModel.fetchVideos(playlistId, apiKey)
                }
            }

            PlaylistScreen(
                studentName = name,
                channelId = playlistId,
                videos = viewModel.videos.value,
                isLoading = viewModel.isLoading.value,
                errorMessage = viewModel.errorMessage.value,
                onBackClick = { navController.popBackStack() },
                onProfileClick = { navController.navigate("profile") },
                onVideoClick = { videoId -> navController.navigate("video_bento/$videoId") },
                onRetryClick = { id -> viewModel.fetchVideos(id, apiKey) }
            )
        }

        composable(
            route = "video_bento/{videoId}",
            arguments = listOf(navArgument("videoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId") ?: ""

            VideoScreen(
                list = viewModel.videos.value,
                initialVideoId = videoId,
                onBack = { navController.popBackStack() },
                onClick = { }
            )
        }

        composable("profile") {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate("login_screen") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
