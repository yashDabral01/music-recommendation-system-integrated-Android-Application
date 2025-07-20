package com.example.musicapplication.RepositoryFile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.musicapplication.Api.LocalServerApiService
import com.example.musicapplication.Api.SpotifyApiService
import com.example.musicapplication.Database.TrackDatabase
import com.example.musicapplication.Models.RecentlyPlayedSong
import com.example.musicapplication.Models.Recommendation
import com.example.musicapplication.Models.RecommendedSongs
import com.example.musicapplication.Models.SongDetails
import com.example.musicapplication.Models.TrackDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Repository(private val localServerApiService: LocalServerApiService,private val spotifyApiService: SpotifyApiService,private val trackDatabase: TrackDatabase ) {
    private val recommendedSongsLiveData = MutableLiveData<Response<RecommendedSongs>>()
    val recommendedSongs: LiveData<Response<RecommendedSongs>>
        get() = recommendedSongsLiveData

    private val trackDetailsLiveData = MutableLiveData<Response<SongDetails>>()
    val trackDetails : LiveData<Response<SongDetails>>
        get() = trackDetailsLiveData

    private val trackDetailsFromRecommendationTableLiveData = MutableLiveData<Recommendation>()
     val trackDetailsFromRecommendationTable : LiveData<Recommendation>
         get() = trackDetailsFromRecommendationTableLiveData


    // Insert recommendation into the database
    suspend fun insertRecommendations(recommendations: List<Recommendation>) {
        trackDatabase.tracksDao().insertRecommendations(recommendations)
    }

    // Insert track details into the database
    suspend fun insertTrackDetails(trackDetails: TrackDetails) {
        trackDatabase.tracksDao().insertTrackDetails(trackDetails)
    }
    val allTrackDetails: LiveData<List<TrackDetails>> = trackDatabase.tracksDao().getAllTrackDetails()
    val allRecentlyPlayedTrackDetails: LiveData<List<RecentlyPlayedSong>> = trackDatabase.tracksDao().getAllRecentlyPlayedTrackDetails()
    // This function will return song details if we pass song name in the parameter
    suspend fun getSongDetails( songName: String) {
        try {
            val authRepository = AuthRepository(clientId ="982a43a7e8b34506b6a2dbba4c13cb7b" , clientSecret ="b9a12e331cc94e3d835339efd17836cd" )
            val token = authRepository.getToken()
            val result = spotifyApiService.searchTracks("track:$songName", token = "Bearer $token")
            if (result.body() != null) {
                trackDetailsLiveData.postValue(Response.Success(result.body()))

                result.body()?.let { songDetails ->
                    val trackDetailsEntity = TrackDetails(
                        serialno = 0,  // Primary key example or auto-generated
                        track_id = songDetails.tracks.items[0].id ?: "Unknown ID",
                        trackname = songDetails.tracks.items[0].name ?: "Unknown Track Name",
                        imageurl = songDetails.tracks.items[0].album.images.getOrNull(1)?.url ?: "No image available",
                        previewurl = (songDetails.tracks.items[0].preview_url ?: "No preview available").toString()
                    )

                    withContext(Dispatchers.IO) {
                        trackDatabase.tracksDao().insertTrackDetails(trackDetailsEntity)
                    }
                }

            }else{
                // Check for 401 Unauthorized error
                if (result.code() == 401) {
                    trackDetailsLiveData.postValue(Response.Error("Token expired, please reauthenticate"))
                } else {
                    trackDetailsLiveData.postValue(Response.Error("API error: ${result.message()}"))
                }
            }
        }catch (e:Exception){
            trackDetailsLiveData.postValue(Response.Error(e.message.toString()))
        }
    }

    //This function will return recommended songs if we pass song name in the parameter
    suspend fun getRecommendSongs( songName: String) {
        try {
            val result = localServerApiService.getRecommendations( songName)
            if (result.body() != null) {
                recommendedSongsLiveData.postValue(Response.Success(result.body()))
                result.body()?.let { recommendedSongsResponse ->
                    val recommendations = recommendedSongsResponse.recommendations
                    insertRecommendations(recommendations)
                    Log.d("Database Insert Recommendations","Successfull")
                }
            }else{
                val errorMessage = result.errorBody()?.string() ?: "Unknown error"
                recommendedSongsLiveData.postValue(Response.Error(errorMessage))
            }
        }catch (e:Exception){
                Log.e("Repository", "Error fetching recommendations", e)
                recommendedSongsLiveData.postValue(Response.Error(e.message.toString()))
        }
    }
    suspend fun getSongDetailsFromRecommendationTable(songName: String) {
        try {
            trackDetailsFromRecommendationTableLiveData.postValue(trackDatabase.tracksDao().getSong(songName))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun getAllSongs(): LiveData<List<Recommendation>> {
        return trackDatabase.tracksDao().getAllSongs()
    }

}