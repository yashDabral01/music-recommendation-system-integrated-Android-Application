package com.example.musicapplication.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapplication.Models.TrackDetails
import com.example.musicapplication.databinding.SquareMusicViewholderBinding

class TrackDetailsAdapter(
    private var trackDetailsList: List<TrackDetails>,
   // private val onItemClick: (TrackDetails) -> Unit
) : RecyclerView.Adapter<TrackDetailsAdapter.TrackDetailsViewHolder>() {

    inner class TrackDetailsViewHolder(val binding: SquareMusicViewholderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackDetailsViewHolder {
        val binding = SquareMusicViewholderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackDetailsViewHolder, position: Int) {
        val trackDetail = trackDetailsList[position]
        holder.binding.apply {
            songName.text = trackDetail.trackname
            Glide.with(songImage.context)
                .load(trackDetail.imageurl)
                .into(songImage)
           // root.setOnClickListener { onItemClick(trackDetail) }
        }
    }

    override fun getItemCount(): Int = trackDetailsList.size

//    fun updateTrackDetails(newTrackDetailsList: List<TrackDetails>) {
//        trackDetailsList = newTrackDetailsList
//        notifyDataSetChanged()
//    }
}
