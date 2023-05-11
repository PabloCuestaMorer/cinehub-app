package es.usj.pcuestam.cinehubapp.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.usj.pcuestam.cinehubapp.R
import es.usj.pcuestam.cinehubapp.adapters.ActorAdapter
import es.usj.pcuestam.cinehubapp.adapters.GenreAdapter
import es.usj.pcuestam.cinehubapp.beans.Movie
import es.usj.pcuestam.cinehubapp.viewmodels.MovieListViewModel

class ViewMovieActivity : AppCompatActivity() {
    private lateinit var movieListViewModel: MovieListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_movie)

        initViewModel()
        fetchMovieDetails()
        observeMovieLiveData()
    }

    private fun initViewModel() {
        movieListViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(MovieListViewModel::class.java)
    }

    private fun fetchMovieDetails() {
        val movieId = intent.getIntExtra("MOVIE_ID", -1)
        if (movieId != -1) {
            movieListViewModel.fetchMovieById(movieId)
        }
    }

    private fun observeMovieLiveData() {
        movieListViewModel.movieLiveData.observe(this) { movie ->
            movie?.let { displayMovieDetails(it) }
        }
    }

    private fun displayMovieDetails(movie: Movie) {
        setMovieInfo(movie)
        setupGenresRecyclerView(movie)
        setupActorsRecyclerView(movie)
    }

    private fun setMovieInfo(movie: Movie) {
        findViewById<TextView>(R.id.movie_title).text = movie.title
        findViewById<TextView>(R.id.movie_description).text = movie.description
        findViewById<TextView>(R.id.movie_year).text = movie.year.toString()
        findViewById<TextView>(R.id.movie_runtime).text = "${movie.runtime} min"
        findViewById<TextView>(R.id.movie_rating).text = movie.rating.toString()
        findViewById<TextView>(R.id.movie_votes).text = movie.votes.toString()
        findViewById<TextView>(R.id.movie_revenue).text = "$${movie.revenue}M"
        findViewById<TextView>(R.id.movie_director).text = movie.director
    }

    private fun setupGenresRecyclerView(movie: Movie) {
        val genresRecyclerView = findViewById<RecyclerView>(R.id.genresRecyclerView)
        genresRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        genresRecyclerView.adapter = GenreAdapter(
            movie.genres.mapNotNull { movieListViewModel.getGenreById(it) }
        ) {
            // Handle genre click here
        }
    }

    private fun setupActorsRecyclerView(movie: Movie) {
        val actorsRecyclerView = findViewById<RecyclerView>(R.id.actorsRecyclerView)
        actorsRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        actorsRecyclerView.adapter = ActorAdapter(
            movie.actors.mapNotNull { movieListViewModel.getActorById(it) }
        ) {
            // Handle actor click here
        }
    }
}
