package com.example.musicapplication.Api

import com.example.musicapplication.Models.RecommendedSongs
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocalServerApiService {
    @GET("recommend")
    suspend fun getRecommendations(
        @Query("song") song : String
    ): Response<RecommendedSongs>
}