package es.usj.pcuestam.cinehubapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import es.usj.pcuestam.cinehubapp.R
import es.usj.pcuestam.cinehubapp.beans.Actor
import es.usj.pcuestam.cinehubapp.beans.Genre
import es.usj.pcuestam.cinehubapp.beans.Movie
import es.usj.pcuestam.cinehubapp.viewmodels.MovieListViewModel

class EditMovieActivity : AppCompatActivity() {
    private var movieId: Int = 0
    private lateinit var movieListViewModel: MovieListViewModel

    private lateinit var movieTitleEditText: EditText
    private lateinit var movieDescriptionEditText: EditText
    private lateinit var movieYearEditText: EditText
    private lateinit var movieLengthEditText: EditText
    private lateinit var movieRatingEditText: EditText
    private lateinit var movieVotesEditText: EditText
    private lateinit var movieRevenueEditText: EditText
    private lateinit var movieDirectorEditText: EditText
    private lateinit var genreSpinner: Spinner
    private lateinit var actorSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_movie)

        movieListViewModel = ViewModelProvider(this)[MovieListViewModel::class.java]

        movieTitleEditText = findViewById(R.id.movie_title_edit_text)
        movieDescriptionEditText = findViewById(R.id.movie_description_edit_text)
        movieYearEditText = findViewById(R.id.movie_release_year_edit_text)
        movieLengthEditText = findViewById(R.id.movie_length_edit_text)
        movieRatingEditText = findViewById(R.id.movie_rating_edit_text)
        movieVotesEditText = findViewById(R.id.movie_votes_edit_text)
        movieRevenueEditText = findViewById(R.id.movie_revenue_edit_text)
        movieDirectorEditText = findViewById(R.id.movie_director_edit_text)
        genreSpinner = findViewById(R.id.movie_genres_spinner)
        actorSpinner = findViewById(R.id.movie_actors_spinner)

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
                    setUpGenreSpinner(genres)
                }
            }

            movieListViewModel.actorListLiveData.observe(this) { actors ->
                if (actors != null) {
                    setUpActorSpinner(actors)
                }
            }
        } else {
            Toast.makeText(this, "Error: Movie ID not found.", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun populateFormWithMovieDetails(movie: Movie) {
        movieTitleEditText.setText(movie.title)
        movieDescriptionEditText.setText(movie.description)
        movieYearEditText.setText(movie.year.toString())
        movieLengthEditText.setText(movie.length.toString())
        movieRatingEditText.setText(movie.rating.toString())
        movieVotesEditText.setText(movie.votes.toString())
        movieRevenueEditText.setText(movie.revenue.toString())
        movieDirectorEditText.setText(movie.director)
    }

    private fun setUpGenreSpinner(genres: List<Genre>) {
        val genreAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            genres.map { it.name }
        )
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genreSpinner.adapter = genreAdapter
    }

    private fun setUpActorSpinner(actors: List<Actor>) {
        val actorAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            actors.map { it.name }
        )
        actorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        actorSpinner.adapter = actorAdapter
    }
}