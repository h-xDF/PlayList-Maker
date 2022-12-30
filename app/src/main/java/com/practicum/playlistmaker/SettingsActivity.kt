package com.practicum.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    companion object {
        const val userEmail = "raralux@yandex.ru"
    }

    private lateinit var sharedAppBtn: ImageButton
    private lateinit var sendSupportAgentBtn: ImageView
    private lateinit var userAgreementBtn: ImageButton
    private lateinit var themeSwitcher: SwitchMaterial
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        pref = getSharedPreferences(getString(R.string.app_preference), MODE_PRIVATE)
        themeSwitcher = findViewById(R.id.theme_switch)
        themeSwitcher.isChecked = pref.getBoolean(getString(R.string.dark_mode_status), false)
        sharedAppBtn = findViewById(R.id.shared_app_btn)
        sendSupportAgentBtn = findViewById(R.id.support_agent_btn)
        userAgreementBtn = findViewById(R.id.agreement_btn)

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            pref.edit()
                .putBoolean(getString(R.string.dark_mode_status), checked)
                .apply()
            (applicationContext as App).switchTheme(checked)
        }

        sharedAppBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_course_link))
            startActivity(intent)
        }

        sendSupportAgentBtn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, userEmail)
            shareIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.email_subject)
            )
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.email_body)
            )
            startActivity(shareIntent)
        }

        userAgreementBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.practicum_offer)))
            startActivity(intent)
        }
    }
}