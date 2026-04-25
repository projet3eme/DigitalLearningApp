package com.example.digitallearningapp.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.serialization.json.Json

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://sdecoarxkwqlhyzxxmpx.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNkZWNvYXJ4a3dxbGh5enh4bXB4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzcwNDYyODksImV4cCI6MjA5MjYyMjI4OX0.5QyFw7L9g44sQCYMEjoz_fIemTF-cT-yldU6nuZU3YA"
    ) {
        install(Postgrest) {
            serializer = io.github.jan.supabase.serializer.KotlinXSerializer(
                Json { ignoreUnknownKeys = true }
            )
        }
    }
}