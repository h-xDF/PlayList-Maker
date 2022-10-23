package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val toastPattern = "You clicked on '%s' button"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn = findViewById<Button>(R.id.search_btn)
        val mediaLibBtn = findViewById<Button>(R.id.media_library_btn)
        val settingBtn = findViewById<Button>(R.id.setting_btn)


        searchBtn.setOnClickListener {
            showToast(String.format(toastPattern, getString(R.string.search_title)))
        }

        mediaLibBtn.setOnClickListener {
            showToast(String.format(toastPattern, getString(R.string.media_library_title)))
        }

        // anonymous class
        val settingClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                showToast(String.format(toastPattern, getString(R.string.setting_title)))
            }
        }
        settingBtn.setOnClickListener(settingClickListener)
    }

    private fun showToast(text: String) {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show()
    }
}