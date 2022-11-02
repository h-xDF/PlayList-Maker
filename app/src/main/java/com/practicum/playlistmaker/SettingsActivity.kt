package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    companion object {
        const val courseLink = "https://practicum.yandex.ru/learn/android-developer/"
        const val userEmail = "raralux@yandex.ru"
        const val practicumOfferLink = "https://yandex.ru/legal/practicum_offer/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedAppBtn = findViewById<ImageButton>(R.id.shared_app_btn)
        val sendSupportAgentBtn = findViewById<ImageView>(R.id.support_agent_btn)
        val userAgreementBtn = findViewById<ImageButton>(R.id.agreement_btn)

        sharedAppBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, courseLink)
            startActivity(intent)
        }

        sendSupportAgentBtn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(userEmail))
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
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(practicumOfferLink))
            startActivity(intent)
        }

    }
}