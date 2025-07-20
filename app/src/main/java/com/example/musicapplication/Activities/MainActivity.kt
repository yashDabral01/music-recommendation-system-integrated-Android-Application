package com.example.musicapplication.Activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapplication.Adapters.RecentlyPlayedSongsAdapter
import com.example.musicapplication.Adapters.TrackDetailsAdapter
import com.example.musicapplication.Database.SongsApplication
import com.example.musicapplication.Database.TrackDatabase
import com.example.musicapplication.R
import com.example.musicapplication.RepositoryFile.AuthRepository
import com.example.musicapplication.RepositoryFile.Repository
import com.example.musicapplication.RepositoryFile.Response
import com.example.musicapplication.ViewModel.MainViewModel
import com.example.musicapplication.ViewModel.MainViewModelFactory
import com.example.musicapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel : MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var trackDetailsAdapter: TrackDetailsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

         modifyStatusBar()
       // getRecommededSongs("Clocks")
        getRecommededSongs("Somebody Told Me")
       // getRecommededSongs("Fix You")
        //getRecommededSongs("Lithium")
      // getRecommededSongs("Cheap Thrills")
        observeAndProcessSongs()
       setupRecyclerViewForRecommendation()
       setupRecyclerViewForRecentlyPlayedTracks()

    }
    private fun setupViewModel() {
        val repository = (application as SongsApplication).repository
        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repository, "") // Default ViewModel initialization
        )[MainViewModel::class.java]
    }

    // Initialize ViewModel with a specific songName when required
    private fun setupViewModelForSong(songName: String): MainViewModel {
        val repository = (application as SongsApplication).repository
        return ViewModelProvider(
            this,
            MainViewModelFactory(repository, songName)
        )[MainViewModel::class.java]
    }
    private fun setupRecyclerViewForRecentlyPlayedTracks() {
        val repository = (application as SongsApplication).repository
        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repository, "")
        )[MainViewModel::class.java]
        mainViewModel.recentlyPlayedSong.observe(this) {
            Log.d("RecentlyPlayed", "Data: ${it.size}")
            binding.recentlyPlayedRecyclerView.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,false)
            binding.recentlyPlayedRecyclerView.adapter = RecentlyPlayedSongsAdapter(it)
        }
    }

    //By using this function function we can get the recommended songs for particular song
    private fun getRecommededSongs(songName : String){
        val repository = (application as SongsApplication).repository
        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repository, songName)
        )[MainViewModel::class.java]
        //this code will be called multiple times
        mainViewModel.recommendedSongs.observe(this) {
            when(it){
                is Response.Loading -> {
                    // Do something while loading
                }
                is Response.Success -> {
                    val recommendedSongs = it.data?.recommendations
                    Log.d("MainActivity", "Recommended Songs for ${songName}:")
                    recommendedSongs?.forEach { recommendation ->
                        Log.d("MainActivity", "Song: ${recommendation.song_name}, Track ID: ${recommendation.track_id}")
                    }
                }
                is Response.Error -> {
                    Toast.makeText(this, "Some error occurred!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getSongDetails(songName : String){
        val repository = (application as SongsApplication).repository
        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repository, songName)
        )[MainViewModel::class.java]
        //this code will be called multiple times
        mainViewModel.songDetails.observe(this) {
            when(it){
                is Response.Loading -> {
                    // Do something while loading
                    Log.d("MainActivity", "loading......")
                }
                is Response.Success -> {
                    val items = it.data?.tracks?.items
                    Log.d("MainActivity", "Details of song :")
                    items?.forEach {  i->
                        Log.d("MainActivity", "Song: ${i.name}, Track ID: ${i.id},Preview url:${i.preview_url}")
//                    Log.d("MainActivity", "${songName} details are fetched and stored in a database")
                }}
                is Response.Error -> {
                    Toast.makeText(this, "Some error occurred!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
   }
    private fun getSongDetailsFromRecommendationTable(songName: String) {
        val repository = (application as SongsApplication).repository
        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repository, songName)
        )[MainViewModel::class.java]

        // Observe the LiveData
        mainViewModel.songDetailsRecommendationTable.observe(this) { recommendation ->
            recommendation?.let {
                // Use it.song_name here
                Log.d("SongName", it.song_name)

                // Update UI or perform any required action
            } ?: run {
                Log.e("Error", "No song found")
            }
        }
    }
    private fun observeAndProcessSongs() {
        val repository = (application as SongsApplication).repository

        // Observe all songs
        repository.getAllSongs().observe(this) { songsList ->
            songsList?.let { songs ->
                // Iterate through each song and process it
                for (song in songs) {
                    // Directly call repository methods for each song
                    processSong(song.song_name,repository)
                }
            }
        }
    }


    // Function to process a song by its name
    private fun processSong(songName: String, repository: Repository) {
        lifecycleScope.launch {
            // Fetch and store song details for the given song name
            repository.getSongDetails(songName)

            // Fetch recommendations for the given song name
            //repository.getRecommendSongs(songName)

            Log.d("SongProcessing", "Processed song: $songName")
        }
    }

    private fun setupRecyclerViewForRecommendation() {
        val repository = (application as SongsApplication).repository
        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repository, "")
        )[MainViewModel::class.java]
        mainViewModel.allTrackDetails.observe(this) {
            binding.recommendedView.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,false)
            binding.recommendedView.adapter = TrackDetailsAdapter(it)
        }
    }
    private fun modifyStatusBar() {
        // Change status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        // Clear the LIGHT_STATUS_BAR flag to ensure white icons
        window.decorView.systemUiVisibility = 0
    }
}