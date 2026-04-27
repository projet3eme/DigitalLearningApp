package com.example.digitallearningapp.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int = 0,
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val level: String? = "",
    val year: String? = "",
    val created_at: String = ""
)