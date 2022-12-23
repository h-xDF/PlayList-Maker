package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.data.SearchAdapter
import com.practicum.playlistmaker.mock.MockTrack

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
    }

    private lateinit var searchTextEdt: EditText
    private lateinit var clearTextBtn: ImageButton
    private lateinit var trackRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchTextEdt = findViewById(R.id.searchEditText)
        clearTextBtn = findViewById(R.id.cleatTextBtn)
        trackRecycler = findViewById(R.id.search_track_rv)
        trackRecycler.layoutManager = LinearLayoutManager(this)
        trackRecycler.adapter = SearchAdapter(MockTrack.tracks())

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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val tempSearchQuery = searchTextEdt.text.toString()
        if (tempSearchQuery.isNotEmpty()) {
            outState.putString(SEARCH_QUERY, tempSearchQuery)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchTextEdt.setText(savedInstanceState.getString(SEARCH_QUERY, ""))
    }
}