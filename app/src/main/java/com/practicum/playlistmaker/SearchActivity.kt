package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchTextEdt = findViewById<EditText>(R.id.searchEditText)
        val clearTextBtn = findViewById<ImageButton>(R.id.cleatTextBtn)

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
}