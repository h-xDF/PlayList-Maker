package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.data.SearchAdapter
import com.practicum.playlistmaker.network.ItunesTransport
import com.practicum.playlistmaker.network.models.ItunesSearchResponse
import com.practicum.playlistmaker.network.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
    }

    private lateinit var searchTextEdt: EditText
    private lateinit var clearTextBtn: ImageButton
    private lateinit var trackRecycler: RecyclerView
    private lateinit var errorImage: ImageView
    private lateinit var errorDescription: TextView
    private lateinit var updateBtn: Button
    private val tracks = ArrayList<Track>()
    private val adapter = SearchAdapter(tracks)

    private var currentSearchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchTextEdt = findViewById(R.id.searchEditText)
        clearTextBtn = findViewById(R.id.cleatTextBtn)
        trackRecycler = findViewById(R.id.search_track_rv)
        trackRecycler.layoutManager = LinearLayoutManager(this)
        trackRecycler.adapter = adapter
        errorImage = findViewById(R.id.error_iv)
        errorDescription = findViewById(R.id.error_tv)
        updateBtn = findViewById(R.id.update_btn)

        hideViewError()

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clearTextBtn.visibility = View.GONE
                } else {
                    clearTextBtn.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // TODO
            }
        }

        searchTextEdt.addTextChangedListener(searchTextWatcher)

        clearTextBtn.setOnClickListener {
            searchTextEdt.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchTextEdt.windowToken, 0)
        }

        searchTextEdt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                currentSearchQuery = searchTextEdt.text.toString()
                songSearchRequest(currentSearchQuery)
                true
            }
            false
        }

        updateBtn.setOnClickListener {
            songSearchRequest(currentSearchQuery)
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val tempSearchQuery = searchTextEdt.text.toString()
        if (tempSearchQuery.isNotEmpty()) {
            outState.putString(SEARCH_QUERY, currentSearchQuery)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedSearchQuery = savedInstanceState.getString(SEARCH_QUERY, "")
        searchTextEdt.setText(savedSearchQuery)
        currentSearchQuery = savedSearchQuery
        songSearchRequest(currentSearchQuery)
    }

    private fun songSearchRequest(text: String) {
        ItunesTransport.client.searchSong(text = text)
            .enqueue(object : Callback<ItunesSearchResponse> {
                override fun onResponse(
                    call: Call<ItunesSearchResponse>,
                    response: Response<ItunesSearchResponse>
                ) {
                    tracks.clear()
                    if (response.isSuccessful) {
                        hideViewError()

                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        } else {
                            errorImage.setImageResource(R.drawable.ic_song_empty_search)
                            errorImage.visibility = View.VISIBLE

                            errorDescription.text = getString(R.string.song_nothing_find)
                            errorDescription.visibility = View.VISIBLE
                        }
                    } else {
                        internetProblemVisible()
                    }
                }

                override fun onFailure(call: Call<ItunesSearchResponse>, t: Throwable) {
                    internetProblemVisible()
                }
            })
    }

    private fun hideViewError() {
        errorImage.visibility = View.GONE
        errorDescription.visibility = View.GONE
        updateBtn.visibility = View.GONE
    }

    private fun internetProblemVisible() {
        errorDescription.visibility = View.VISIBLE
        errorDescription.text = getString(R.string.song_not_internet)

        errorImage.setImageResource(R.drawable.ic_song_internet)
        errorImage.visibility = View.VISIBLE

        updateBtn.visibility = View.VISIBLE
    }
}