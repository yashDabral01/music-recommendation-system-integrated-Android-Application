package com.example.musicapplication.Database

import android.app.Application
import com.example.musicapplication.Api.LocalServerApiService
import com.example.musicapplication.Api.RetroFitHelper
import com.example.musicapplication.Api.SpotifyApiService
import com.example.musicapplication.RepositoryFile.Repository

class SongsApplication : Application(){
    lateinit var repository: Repository
    override fun onCreate() {
        super.onCreate()
        initialize()
    }
    private fun initialize() {
        val localServerApiService = RetroFitHelper.getLocalServerInstance().create(
            LocalServerApiService::class.java)
        val spotifyApiService = RetroFitHelper.getSpotifyInstance().create(
            SpotifyApiService::class.java)
        val database = TrackDatabase.getDatabase(applicationContext)
        repository = Repository(localServerApiService,spotifyApiService,database)
    }
}