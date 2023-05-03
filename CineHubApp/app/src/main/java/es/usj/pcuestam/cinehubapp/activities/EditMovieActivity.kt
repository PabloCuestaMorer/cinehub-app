package es.usj.pcuestam.cinehubapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import es.usj.pcuestam.cinehubapp.R

class EditMovieActivity : AppCompatActivity() {
    private var movieId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_movie)

        movieId = intent.getIntExtra("MOVIE_ID", 0)
        if (0 < movieId) {
            // Fetch movie details and populate the form
            Toast.makeText(this, "Edit movie id: $movieId", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Error: Movie ID not found.", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}