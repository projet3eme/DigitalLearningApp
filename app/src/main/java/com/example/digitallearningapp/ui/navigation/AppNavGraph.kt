package com.example.digitallearningapp.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.digitallearningapp.screens.*
import com.example.digitallearningapp.viewmodel.PlaylistViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {
    val viewModel: PlaylistViewModel = viewModel()
    val apiKey = "AIzaSyC3VzbxUXNJHp_B3xjuSFUpjr3FzWFLSBg"

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // 1. الشاشة الترحيبية
        composable("splash") {
            SplashScreen(onClick = { navController.navigate("login_screen") })
        }

        // 2. شاشة تسجيل الدخول
        composable("login_screen") {
            var name by remember { mutableStateOf("") }
            var selectedLevel by remember { mutableStateOf("") }

            LoginScreen(
                name = name,
                onNameChange = { name = it },
                selectedLevel = selectedLevel,
                onLevelSelected = { selectedLevel = it },
                onLoginSuccess = {
                    navController.navigate("years_screen/$selectedLevel/$name")
                },
                onCreateAccountClick = {}
            )
        }

        // 3. شاشة السنوات
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

        // 4. شاشة المواد - توليد المفاتيح (p3_math, m1_science, s1_physics, etc.)
        composable(
            route = "subjects_screen/{level}/{year}/{name}",
            arguments = listOf(
                navArgument("level") { type = NavType.StringType },
                navArgument("year") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val levelArg = backStackEntry.arguments?.getString("level") ?: ""
            val yearArg = backStackEntry.arguments?.getString("year") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""

            SubjectScreen(
                level = levelArg,
                year = yearArg,
                onBack = { navController.popBackStack() },
                onSubjectClick = { level, year, subject ->
                    // تحديد البادئة بناءً على المرحلة (p = ابتدائي، m = متوسط، s = ثانوي)
                    val prefix = when {
                        level.contains("ابتد") -> "p"
                        level.contains("متوسط") -> "m"
                        else -> "s"
                    }

                    // تحديد رقم السنة
                    val yearNum = when {
                        year.contains("الأولى") -> "1"
                        year.contains("الثانية") -> "2"
                        year.contains("الثالثة") -> "3"
                        year.contains("الرابعة") -> "4"
                        year.contains("الخامسة") -> "5"
                        else -> "1"
                    }

                    // تحديد كود المادة
                    val subjectCode = when {
                        subject.contains("رياض") -> "math"
                        subject.contains("عرب") -> "arabic"
                        subject.contains("إسلام") -> "islamic"
                        subject.contains("علوم") -> "science"
                        subject.contains("فيزيا") -> "physics"
                        subject.contains("كيميا") -> "chemistry"
                        else -> "other"
                    }

                    val uniqueKey = "${prefix}${yearNum}_$subjectCode"
                    navController.navigate("playlist_screen/$uniqueKey/$name")
                }
            )
        }

        // 5. شاشة الدروس - ربط المفاتيح بمعرفات يوتيوب الفعلية
        composable(
            route = "playlist_screen/{subjectKey}/{name}",
            arguments = listOf(
                navArgument("subjectKey") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val subjectKey = backStackEntry.arguments?.getString("subjectKey") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""

            // الربط البرمي: ضعي معرفات الـ PL هنا لكل مفتاح
            val playlistId = when (subjectKey) {
                // --- المرحلة الابتدائية (Primary) ---
                "p1_math" -> "PLb3BsV2OPANLuW_8uF08cYXYN6I7EUV5L"
                "p1_arabic" -> "PLb3BsV2OPANIH0M6cOyfJmexazmM1n2DP"
                "p2_math" -> "PLb3BsV2OPANIr_HQnrwFQFBFQZYZ3yoTv"
                "p2_arabic" -> "PLb3BsV2OPANIpIXIjtGIaV93eiTaxNTt0"
                "p3_math" -> "PLb3BsV2OPANJ5tfJYGj34Jzx8TN2xCMqi"
                "p3_arabic" -> "PLb3BsV2OPANJkMFIc9u8hLkW-8k4wQDaX"
                "p3_islamic" -> "PLb3BsV2OPANLgu6I9inNVW-wevS3FouYV"

                // --- المرحلة المتوسطة (Middle) ---
                "m1_math" -> "PLslW4owcXA5rWLxEfeyi40nGXUdFp7jPL"
                "m1_science" -> "PLslW4owcXA5o3cMFBLsHM6gBtfWb9tCqS"
                "m1_arabic" -> "PLslW4owcXA5q9HMeabfiru2J21QU5c3B3"
                "m2_math" -> "PLslW4owcXA5qxvYPS1jIAj_mxMrqngaoM"
                "m2_science" -> "PLslW4owcXA5pMhBxsP61dfKzDn-Ahu7hM"
                "m2_arabic" -> "PLslW4owcXA5rJnC0fE3TcvAOHtVF_yTsN"
                "m3_math" -> "PLslW4owcXA5pOi3d9sxy0YyJbiNBP-O8H"
                "m3_science" -> "PLslW4owcXA5pzdt-hHlU4NtgarCqtgRQo"
                "m3_arabic" -> "PLslW4owcXA5oP1Th6-SA1tRKmPF9bNt1k"

                // --- المرحلة الثانوية (Secondary) ---
                "s1_math" -> "PLslW4owcXA5rdaTXEseNOQq2kszcTZhvg"
                "s1_physics" -> ""
                "s1_chemistry" -> ""
                "s1_arabic" -> "PLslW4owcXA5qPjnDGMKCPVeCBORUp7WSd"

                "s2_math" -> "PLslW4owcXA5pXCGVEYzZ4g80PT4RlvlK7"
                "s2_physics" -> ""
                "s2_chemistry" -> ""
                "s2_arabic" -> "PLslW4owcXA5oMJCWojIeS2sClQl5N48U5"

                "s3_math" -> "PLslW4owcXA5pEk1myV7KDyC2tsnS3qfHU"
                "s3_physics" -> ""
                "s3_chemistry" -> ""
                "s3_arabic" -> "PLslW4owcXA5qrknYUruobCUhY1BVmEF4y"

                else -> "PLb3BsV2OPANJ_ie17N1sZs13ZjC0UrKXS" // المعرف الافتراضي
            }

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
                onVideoClick = { videoId ->
                    navController.navigate("video_bento/$videoId")
                },
                onRetryClick = { id -> viewModel.fetchVideos(id, apiKey) }
            )
        }

        // 6. واجهة الفيديو الشاملة
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

        // 7. الملف الشخصي
        composable("profile") {
            ProfileScreen(onBack = { navController.popBackStack() })
        }
    }
}