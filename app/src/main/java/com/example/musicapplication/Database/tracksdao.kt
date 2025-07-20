package com.example.musicapplication.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicapplication.Models.RecentlyPlayedSong
import com.example.musicapplication.Models.Recommendation
import com.example.musicapplication.Models.TrackDetails
import com.example.musicapplication.Models.Tracks
import retrofit2.http.GET

@Dao
interface tracksdao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecommendations(recommendation: List<Recommendation>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecentlyPlayed(recently:RecentlyPlayedSong )
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackDetails(songDetails: TrackDetails)

    @Query("SELECT * FROM recommendation")
    fun getAllSongs(): LiveData<List<Recommendation>>

    @Query("SELECT * FROM recommendation")
    suspend fun getRecommendations(): List<Recommendation>

    @Query("SELECT * From recommendation where song_name =:song")
    suspend fun getSong(song: String): Recommendation

    @Query("SELECT * FROM trackdetails")
    fun getAllTrackDetails(): LiveData<List<TrackDetails>>

    @Query("SELECT * FROM recentlyplayedsong")
    fun getAllRecentlyPlayedTrackDetails(): LiveData<List<RecentlyPlayedSong>>
}