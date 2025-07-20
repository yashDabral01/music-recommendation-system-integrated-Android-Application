package com.example.musicapplication.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapplication.Models.RecentlyPlayedSong
import com.example.musicapplication.Models.Recommendation
import com.example.musicapplication.Models.RecommendedSongs
import com.example.musicapplication.Models.SongDetails
import com.example.musicapplication.Models.TrackDetails
import com.example.musicapplication.RepositoryFile.AuthRepository
import com.example.musicapplication.RepositoryFile.Repository
import com.example.musicapplication.RepositoryFile.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class MainViewModel(private val repository: Repository, private val songName: String) : ViewModel() {
    val recentlyPlayedSong:LiveData<List<RecentlyPlayedSong>> = repository.allRecentlyPlayedTrackDetails
    val recommendedSongs: LiveData<Response<RecommendedSongs>> = repository.recommendedSongs
    val songDetails: LiveData<Response<SongDetails>> = repository.trackDetails
    val songDetailsRecommendationTable :LiveData<Recommendation> = repository.trackDetailsFromRecommendationTable
    val allSongs: LiveData<List<Recommendation>> = repository.getAllSongs()
    val allTrackDetails: LiveData<List<TrackDetails>> = repository.allTrackDetails
    init {
        loadSongData()
    }

    private fun loadSongData() {
        viewModelScope.launch(Dispatchers.IO) {
            // Call the repository methods to load data
            repository.getRecommendSongs(songName)
            repository.getSongDetails(songName)
            repository.getSongDetailsFromRecommendationTable(songName)
        }
    }
}
