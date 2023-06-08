package es.usj.pcuestam.cinehubapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.usj.pcuestam.cinehubapp.databinding.ActivityContactBinding // Importa la clase de binding

class ContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.emailTextView.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:fake.email@example.com")
                putExtra(Intent.EXTRA_SUBJECT, "Contact from the app")
            }
            startActivity(emailIntent)
        }

        binding.websiteTextView.setOnClickListener {
            val websiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))
            startActivity(websiteIntent)
        }

        binding.phoneTextView.setOnClickListener {
            val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+15551234567"))
            startActivity(phoneIntent)
        }
    }
}
