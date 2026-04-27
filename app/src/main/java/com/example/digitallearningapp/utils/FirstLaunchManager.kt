package com.example.digitallearningapp.utils

import android.content.Context

class FirstLaunchManager(context: Context) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val FIRST_LAUNCH_KEY = "is_first_launch"

    fun isFirstLaunch(): Boolean {
        return prefs.getBoolean(FIRST_LAUNCH_KEY, true)
    }

    fun setLaunched() {
        prefs.edit().putBoolean(FIRST_LAUNCH_KEY, false).apply()
    }
}