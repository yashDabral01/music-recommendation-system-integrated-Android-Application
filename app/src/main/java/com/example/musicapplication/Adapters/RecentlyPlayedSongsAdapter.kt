package com.example.musicapplication.Adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapplication.Models.RecentlyPlayedSong
import com.example.musicapplication.Models.TrackDetails
import com.example.musicapplication.databinding.SquareMusicViewholderBinding

class RecentlyPlayedSongsAdapter(
    private var recentlyPlayedTrackDetailsList: List<RecentlyPlayedSong>,
    // private val onItemClick: (TrackDetails) -> Unit
) : RecyclerView.Adapter<RecentlyPlayedSongsAdapter.recentlyPlayedTrackViewholder>() {

    inner class recentlyPlayedTrackViewholder(val binding: SquareMusicViewholderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): recentlyPlayedTrackViewholder {
        val binding = SquareMusicViewholderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return recentlyPlayedTrackViewholder(binding)
    }

    override fun onBindViewHolder(holder: recentlyPlayedTrackViewholder, position: Int) {
        val trackDetail = recentlyPlayedTrackDetailsList[position]
        holder.binding.apply {
            songName.text = trackDetail.trackname
            Glide.with(songImage.context)
                .load(trackDetail.imageurl)
                .into(songImage)
            // root.setOnClickListener { onItemClick(trackDetail) }
        }
    }

    override fun getItemCount(): Int = recentlyPlayedTrackDetailsList.size

//    fun updateTrackDetails(newTrackDetailsList: List<TrackDetails>) {
//        trackDetailsList = newTrackDetailsList
//        notifyDataSetChanged()
//    }
}
