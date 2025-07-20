package com.example.musicapplication.Models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "recommendation",
    indices = [Index(value = ["song_name"], unique = true)])
data class Recommendation(
    @PrimaryKey(autoGenerate = true)
    val serialno : Int,
    val song_name: String,
    val track_id: String
)
