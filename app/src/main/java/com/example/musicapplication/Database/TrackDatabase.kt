package com.example.musicapplication.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.musicapplication.Models.RecentlyPlayedSong
import com.example.musicapplication.Models.Recommendation
import com.example.musicapplication.Models.TrackDetails

@Database(entities = [TrackDetails::class,Recommendation::class,RecentlyPlayedSong::class], version = 1)
abstract class TrackDatabase : RoomDatabase(){

    abstract fun tracksDao() : tracksdao
    companion object{
        @Volatile
        private var INSTANCE : TrackDatabase? = null
        fun getDatabase(context: Context):TrackDatabase{
            if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context,TrackDatabase::class.java,"tracks_database")
                        //.fallbackToDestructiveMigration()  //this will reset the database
                        .build()

                }
            }
            return INSTANCE!!
        }
    }
}