package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.network.models.Track

class SearchHistory(private val pref: SharedPreferences, private val prefKey: String) {
    private val jsonTemp = Gson()

    fun addTrack(track: Track) {
        var trackList = getHistory().toMutableList()

        trackList.removeIf { it.trackId == track.trackId }
        trackList.add(0, track)
        if (trackList.size > 10) {
            trackList = trackList.subList(0, 10)
        }

        val json = jsonTemp.toJson(trackList)

        pref.edit()
            .putString(prefKey, json)
            .apply()
    }

    fun clearHistory() {
        val json = jsonTemp.toJson(emptyArray<Track>())
        pref.edit()
            .putString(prefKey, json)
            .apply()
    }

    fun getHistory(): Array<Track> {
        val json = pref.getString(prefKey, "")
        return jsonTemp.fromJson(json, Array<Track>::class.java) ?: emptyArray()
    }
}