package com.practicum.playlistmaker.network.models

import com.google.gson.annotations.SerializedName

data class ItunesSearchResponse(
    @SerializedName("resultsCount") val resultsCount: Int,
    @SerializedName("results") val results: List<Track>,
)
