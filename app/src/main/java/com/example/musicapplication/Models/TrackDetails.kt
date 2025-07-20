package com.example.musicapplication.Models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "trackdetails",
    indices = [Index(value = ["track_id"], unique = true)])
data class TrackDetails(
    @PrimaryKey(autoGenerate = true)
    val serialno : Int,
    val track_id : String,
    val trackname : String,
    val imageurl: String,
    val previewurl : String
)
