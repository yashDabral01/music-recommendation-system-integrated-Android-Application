package com.example.musicapplication.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicapplication.RepositoryFile.Repository

class MainViewModelFactory(private val repository: Repository,private val songName : String): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository,songName) as T
    }

}