package com.example.musicapplication.Models

data class Item(
    val album: Album,
    val artists: List<ArtistX>,
    val duration_ms: Int,
    val id: String,
    val name: String,
    val popularity: Int,
    val preview_url: Any,
    val track_number: Int,
    val type: String,
    val uri: String
)