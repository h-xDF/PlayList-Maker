package com.practicum.playlistmaker.network

import com.practicum.playlistmaker.network.models.ItunesSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {

    @GET("/search")
    fun searchSong(
        @Query("entity") entity: String? = "song",
        @Query("term") text: String,
    ): Call<ItunesSearchResponse>
}