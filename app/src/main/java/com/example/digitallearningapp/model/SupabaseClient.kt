package com.example.digitallearningapp.network

import com.example.digitallearningapp.model.Student
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
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

    suspend fun saveStudent(name: String, level: String, year: String): Boolean {
        return try {
            client.postgrest["students"].insert(
                mapOf(
                    "name" to name,
                    "level" to level,
                    "year" to year
                )
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getStudents(): List<Student> {
        return try {
            client.postgrest["students"]
                .select()
                .decodeList<Student>()
        } catch (e: Exception) {
            emptyList()
        }
    }
}