package com.example.musicapplication.Api

import com.example.musicapplication.Models.SongDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyApiService {
    @GET("v1/search")
    suspend fun searchTracks(
        @Query("q") query: String,
        @Query("type") type: String = "track",
        @Query("limit") limit: Int = 1,
        @Header("Authorization") token: String
    ): Response<SongDetails>
}