package com.example.musicapplication.Models

import androidx.room.Entity


data class RecommendedSongs(
    val recommendations: List<Recommendation>,
    val song: String
)