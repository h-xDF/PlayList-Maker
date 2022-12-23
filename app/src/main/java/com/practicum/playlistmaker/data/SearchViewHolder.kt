package com.practicum.playlistmaker.data

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.network.models.Track
import java.text.SimpleDateFormat
import java.util.*

class SearchViewHolder(trackView: View) : RecyclerView.ViewHolder(trackView) {
    private val trackImageView = trackView.findViewById<ImageView>(R.id.cover_iv)
    private val trackNameTextView = trackView.findViewById<TextView>(R.id.track_name_tv)
    private val artistNameTextView = trackView.findViewById<TextView>(R.id.artist_name_tv)
    private val trackTimeTextView = trackView.findViewById<TextView>(R.id.track_time_tv)

    fun bind(track: Track) {
        artistNameTextView.text = track.artistName
        trackNameTextView.text = track.trackName
        trackTimeTextView.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_audiotrack_44)
            .centerCrop()
            .transform(RoundedCorners(5))
            .into(trackImageView)
    }
}