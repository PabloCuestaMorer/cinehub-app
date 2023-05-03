package es.usj.pcuestam.cinehubapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import es.usj.pcuestam.cinehubapp.R

class ContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        val websiteTextView = findViewById<TextView>(R.id.websiteTextView)
        val phoneTextView = findViewById<TextView>(R.id.phoneTextView)

        emailTextView.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:fake.email@example.com")
                putExtra(Intent.EXTRA_SUBJECT, "Contact from the app")
            }
            startActivity(emailIntent)
        }

        websiteTextView.setOnClickListener {
            val websiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))
            startActivity(websiteIntent)
        }

        phoneTextView.setOnClickListener {
            val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+15551234567"))
            startActivity(phoneIntent)
        }
    }
}