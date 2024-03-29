package com.practicum.playlistmaker.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.network.models.Track

class SearchHistoryAdapter(
    private var tracks: Array<Track>,
    private var itemClickListener: (Track) -> Unit
) : RecyclerView.Adapter<SearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return SearchViewHolder(layout)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { itemClickListener(tracks[position]) }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun isNotEmpty(): Boolean {
        return tracks.isNotEmpty()
    }

    fun update(trackUpdate: Array<Track>) {
        tracks = trackUpdate
        notifyDataSetChanged()
    }
}