package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
    }

    private lateinit var searchTextEdt: EditText
    private lateinit var clearTextBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

       searchTextEdt = findViewById(R.id.searchEditText)
       clearTextBtn = findViewById(R.id.cleatTextBtn)

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