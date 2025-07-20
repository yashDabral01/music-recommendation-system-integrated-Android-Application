package com.example.musicapplication.Api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitHelper {
    private const val SPOTIFY_BASE_URL = "https://api.spotify.com/"
    private const val LOCAL_SERVER_URL = "http://10.0.2.2:5000/"
  // private const val LOCAL_SERVER_URL = "http://192.168.37.156:5000/"
    fun getLocalServerInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LOCAL_SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun getSpotifyInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SPOTIFY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}