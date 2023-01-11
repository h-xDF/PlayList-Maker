package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val pref = getSharedPreferences(getString(R.string.app_preference), MODE_PRIVATE)
        darkTheme = pref.getBoolean(getString(R.string.dark_mode_status), false)

        if (darkTheme) {
            switchTheme(true)
        } else {
            switchTheme(false)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}