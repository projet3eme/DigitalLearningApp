package com.example.digitallearningapp.utils

import android.content.Context
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("theme_prefs")

object ThemeManager {
    val THEME_KEY = booleanPreferencesKey("is_dark_mode")

    suspend fun saveThemePreference(context: Context, isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDark
        }
    }

    // تم التعديل لاستقبال القيمة الافتراضية وحل خطأ الـ Composable
    fun getThemePreference(context: Context, defaultValue: Boolean): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: defaultValue
        }
    }
}

@Composable
fun DigitalLearningTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
    val view = LocalView.current

    SideEffect {
        val window = (view.context as androidx.activity.ComponentActivity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

private fun darkColorScheme() = darkColorScheme(
    primary = Color(0xFF4A90E2),
    secondary = Color(0xFF2C5282),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private fun lightColorScheme() = lightColorScheme(
    primary = Color(0xFF4A90E2),
    secondary = Color(0xFF2C5282),
    background = Color(0xFFF0F4FF),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1A3A6B),
    onSurface = Color(0xFF1A3A6B)
)