package es.usj.pcuestam.cinehubapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import es.usj.pcuestam.cinehubapp.R
import es.usj.pcuestam.cinehubapp.beans.Actor
import es.usj.pcuestam.cinehubapp.beans.Genre
import es.usj.pcuestam.cinehubapp.beans.Movie
import es.usj.pcuestam.cinehubapp.viewmodels.MovieListViewModel

class EditMovieActivity : AppCompatActivity() {
    private var movieId: Int = 0
    private lateinit var movieListViewModel: MovieListViewModel

    private lateinit var idEt: TextView
    private lateinit var titleEt: EditText
    private lateinit var descriptionEt: EditText
    private lateinit var yearEt: EditText
    private lateinit var runtimeEt: EditText
    private lateinit var ratingEt: EditText
    private lateinit var votesEt: EditText
    private lateinit var revenueEt: EditText
    private lateinit var movieDirectorEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_movie)

        movieListViewModel = ViewModelProvider(this)[MovieListViewModel::class.java]

        idEt = findViewById(R.id.id_et)
        titleEt = findViewById(R.id.title_et)
        descriptionEt = findViewById(R.id.description_et)
        yearEt = findViewById(R.id.release_year_et)
        runtimeEt = findViewById(R.id.runtime_et)
        ratingEt = findViewById(R.id.rating_et)
        votesEt = findViewById(R.id.votes_et)
        revenueEt = findViewById(R.id.revenue_et)
        movieDirectorEditText = findViewById(R.id.movie_director_edit_text)

        movieId = intent.getIntExtra("MOVIE_ID", 0)
        if (0 < movieId) {
            movieListViewModel.fetchMovieById(movieId)
            movieListViewModel.movieLiveData.observe(this) { movie ->
                if (movie != null) {
                    populateFormWithMovieDetails(movie)
                } else {
                    Toast.makeText(this, "Error: Movie details not found.", Toast.LENGTH_LONG)
                        .show()
                    finish()
                }
            }
            movieListViewModel.loadGenresAndActors()
            movieListViewModel.genreListLiveData.observe(this) { genres ->
                if (genres != null) {
                    movieListViewModel.movieLiveData.observe(this) { movie ->
                        if (movie != null) {
                            setUpGenreSpinner(genres, movie.genres)
                        }
                    }
                }
            }

            movieListViewModel.actorListLiveData.observe(this) { actors ->
                if (actors != null) {
                    movieListViewModel.movieLiveData.observe(this) { movie ->
                        if (movie != null) {
                            setUpActorSpinner(actors, movie.actors)
                        }
                    }
                }
            }

        } else {
            Toast.makeText(this, "Error: Movie ID not found.", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun populateFormWithMovieDetails(movie: Movie) {
        idEt.text = movie.id.toString()
        titleEt.setText(movie.title)
        descriptionEt.setText(movie.description)
        yearEt.setText(movie.year.toString())
        runtimeEt.setText(movie.runtime.toString())
        ratingEt.setText(movie.rating.toString())
        votesEt.setText(movie.votes.toString())
        revenueEt.setText(movie.revenue.toString())
        movieDirectorEditText.setText(movie.director)
    }

    private fun setUpGenreSpinner(genres: List<Genre>, movieGenresIds: List<Int>) {
        val genreAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            genres.map { it.name }
        )
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val genreSpinnersContainer = findViewById<LinearLayout>(R.id.genre_spinners_container)
        genreSpinnersContainer.removeAllViews()

        movieGenresIds.forEach { movieGenreId ->
            val spinner = createGenreSpinner()
            spinner.adapter = genreAdapter

            val actorPosition = genres.indexOfFirst { it.id == movieGenreId }
            if (actorPosition != -1) {
                spinner.setSelection(actorPosition)
            }

            genreSpinnersContainer.addView(spinner)
        }
    }

    private fun createGenreSpinner(): Spinner {
        val spinner = Spinner(this)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 8, 0, 8)
        spinner.layoutParams = layoutParams
        return spinner
    }

    private fun setUpActorSpinner(actors: List<Actor>, movieActorIds: List<Int>) {
        val actorAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            actors.map { it.name }
        )
        actorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val actorSpinnersContainer = findViewById<LinearLayout>(R.id.actor_spinners_container)
        actorSpinnersContainer.removeAllViews()

        movieActorIds.forEach { movieActorId ->
            val spinner = createActorSpinner()
            spinner.adapter = actorAdapter

            val actorPosition = actors.indexOfFirst { it.id == movieActorId }
            if (actorPosition != -1) {
                spinner.setSelection(actorPosition)
            }

            actorSpinnersContainer.addView(spinner)
        }
    }


    private fun createActorSpinner(): Spinner {
        val spinner = Spinner(this)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 8, 0, 8)
        spinner.layoutParams = layoutParams
        return spinner
    }

}