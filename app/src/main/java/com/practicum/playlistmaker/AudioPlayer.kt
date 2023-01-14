package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.data.NavigationKey
import com.practicum.playlistmaker.network.models.Track

class AudioPlayer : AppCompatActivity() {

    private lateinit var backBtn: ImageView
    private lateinit var cover: ImageView
    private lateinit var currentDuration: TextView

    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var totalDuration: TextView
    private lateinit var albumName: TextView
    private lateinit var trackYear: TextView
    private lateinit var trackStyle: TextView
    private lateinit var trackCountry: TextView

    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        track = intent.getSerializableExtra(NavigationKey.SAVE_TRACK.name) as Track

        initUi()
        updateUI(track)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putSerializable(NavigationKey.SAVE_TRACK.name, track)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        track = savedInstanceState.getSerializable(NavigationKey.SAVE_TRACK.name) as Track
    }

    private fun initUi() {
        backBtn = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()
        }
        cover = findViewById(R.id.cover_iv)
        trackName = findViewById(R.id.track_name_tv)
        artistName = findViewById(R.id.artist_name_tv)
        currentDuration = findViewById(R.id.current_duration_tv)
        totalDuration = findViewById(R.id.duration_value)
        albumName = findViewById(R.id.album_value)
        trackYear = findViewById(R.id.year_value)
        trackStyle = findViewById(R.id.style_value)
        trackCountry = findViewById(R.id.country_value)

        // note value
        currentDuration.text = "00:00"
    }

    private fun updateUI(track: Track) {
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_default_track)
            .centerCrop()
            .into(cover)

        trackName.text = track.trackName
        artistName.text = track.artistName
        totalDuration.text = track.getDurationFormat()
        albumName.text = track.collectionName
        trackYear.text = track.releaseDate
        trackStyle.text = track.primaryGenreName
        trackCountry.text = track.country
    }
}