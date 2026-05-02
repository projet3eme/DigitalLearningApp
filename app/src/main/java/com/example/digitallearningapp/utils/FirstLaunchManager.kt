package com.example.digitallearningapp.utils

import android.content.Context

class FirstLaunchManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun isFirstLaunch(): Boolean = prefs.getBoolean("is_first_launch", true)

    fun setLaunched() = prefs.edit().putBoolean("is_first_launch", false).apply()
}