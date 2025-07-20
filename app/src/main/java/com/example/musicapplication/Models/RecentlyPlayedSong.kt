package com.example.musicapplication.Models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
@Entity(tableName = "recentlyplayedsong",
    indices = [Index(value = ["trackname"], unique = true)])
data class RecentlyPlayedSong(
@PrimaryKey(autoGenerate = true)
val serialno : Int,
val track_id : String,
val trackname : String,
val imageurl: String,
val previewurl : String

)
