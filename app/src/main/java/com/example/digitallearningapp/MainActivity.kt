package com.example.digitallearningapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.rememberNavController
import com.example.digitallearningapp.ui.navigation.AppNavGraph

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                // تعريف الـ navController
                val navController = rememberNavController()
                
                // استدعاء AppNavGraph وتمرير الـ navController له
                AppNavGraph(navController = navController)
            }
        }
    }
}
