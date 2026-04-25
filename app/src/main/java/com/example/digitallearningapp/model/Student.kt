package com.example.digitallearningapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val id: String = "",
    val name: String = "",
    val level: String = "",
    val year: String = "",
    val created_at: String = ""
)