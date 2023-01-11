package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
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
import com.practicum.playlistmaker.data.SearchHistoryAdapter
import com.practicum.playlistmaker.network.ItunesTransport
import com.practicum.playlistmaker.network.models.ItunesSearchResponse
import com.practicum.playlistmaker.network.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
        const val HISTORY_PREF_KEY = "song_history"
    }

    private lateinit var pref: SharedPreferences
    private lateinit var searchHistory: SearchHistory
    private lateinit var searchTextEdt: EditText
    private lateinit var clearTextBtn: ImageButton
    private lateinit var trackRecycler: RecyclerView
    private lateinit var errorImage: ImageView
    private lateinit var errorDescription: TextView
    private lateinit var updateBtn: Button
    private lateinit var trackHistoryLayout: LinearLayout
    private lateinit var historyRecycler: RecyclerView
    private lateinit var clearHistoryBtn: Button
    private lateinit var adapter: SearchAdapter
    private lateinit var historyAdapter: SearchHistoryAdapter

    private val tracks = ArrayList<Track>()

    private var currentSearchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        pref = getSharedPreferences(getString(R.string.app_preference), MODE_PRIVATE)
        searchHistory = SearchHistory(pref, HISTORY_PREF_KEY)
        adapter = SearchAdapter(tracks, searchHistory)
        historyAdapter = SearchHistoryAdapter(searchHistory.getHistory())

        searchTextEdt = findViewById(R.id.searchEditText)
        clearTextBtn = findViewById(R.id.cleatTextBtn)
        trackRecycler = findViewById(R.id.search_track_rv)
        trackRecycler.layoutManager = LinearLayoutManager(this)
        trackRecycler.adapter = adapter
        errorImage = findViewById(R.id.error_iv)
        errorDescription = findViewById(R.id.error_tv)
        updateBtn = findViewById(R.id.update_btn)
        trackHistoryLayout = findViewById(R.id.track_history_vg)
        historyRecycler = findViewById(R.id.history_rv)
        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = historyAdapter
        clearHistoryBtn = findViewById(R.id.history_clear_btn)

        hideViewError()
        initViewState()

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clearTextBtn.visibility = View.GONE
                    trackHistoryLayout.visibility = View.VISIBLE
                } else {
                    hideViewError()
                    clearTextBtn.visibility = View.VISIBLE
                    trackHistoryLayout.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // TODO
            }
        }

        searchTextEdt.addTextChangedListener(searchTextWatcher)
        searchTextEdt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchTextEdt.text.isEmpty() && historyAdapter.isNotEmpty()) {
                hideViewError()
                trackHistoryLayout.visibility = View.VISIBLE
            } else {
                View.GONE
            }
        }

        clearHistoryBtn.setOnClickListener {
            searchHistory.clearHistory()
            trackHistoryLayout.visibility = View.GONE
            hideViewError()
        }

        clearTextBtn.setOnClickListener {
            tracks.clear()
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

        pref.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        pref.unregisterOnSharedPreferenceChangeListener(this)
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
                    trackHistoryLayout.visibility = View.GONE
                    if (response.isSuccessful) {
                        hideViewError()

                        if ((response.body()?.resultCount ?: 0) > 0) {
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

    fun initViewState() {
        if (historyAdapter.isNotEmpty()) {
            trackHistoryLayout.visibility = View.VISIBLE
        } else {
            trackHistoryLayout.visibility = View.GONE
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == HISTORY_PREF_KEY) {
            historyAdapter.update(searchHistory.getHistory())
        }
    }
}