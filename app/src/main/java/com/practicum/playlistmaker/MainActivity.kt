package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        mediaLibBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, MediaLibraryActivity::class.java))
        }

        settingBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show()
    }
}