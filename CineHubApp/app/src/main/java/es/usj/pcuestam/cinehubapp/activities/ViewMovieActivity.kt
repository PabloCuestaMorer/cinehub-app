package es.usj.pcuestam.cinehubapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import es.usj.pcuestam.cinehubapp.R
import es.usj.pcuestam.cinehubapp.adapters.ActorAdapter
import es.usj.pcuestam.cinehubapp.adapters.GenreAdapter
import es.usj.pcuestam.cinehubapp.beans.Movie
import es.usj.pcuestam.cinehubapp.databinding.ActivityViewMovieBinding
import es.usj.pcuestam.cinehubapp.viewmodels.MovieListViewModel

class ViewMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewMovieBinding
    private lateinit var movieListViewModel: MovieListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        fetchMovieDetails()
        observeMovieLiveData()
    }

    private fun initViewModel() {
        movieListViewModel = ViewModelProvider(this)[MovieListViewModel::class.java]
    }

    private fun fetchMovieDetails() {
        val movieId = intent.getIntExtra("MOVIE_ID", -1)
        if (movieId != -1) {
            movieListViewModel.fetchMovieById(movieId)
        }
    }

    private fun observeMovieLiveData() {
        movieListViewModel.movieLiveData.observe(this) { movie ->
            movie?.let {
                displayMovieDetails(it)
                movieListViewModel.loadGenresAndActors()
                observeGenresAndActorsLiveData()
            }
        }
    }

    private fun observeGenresAndActorsLiveData() {
        movieListViewModel.genreListLiveData.observe(this) { genres ->
            genres?.let {
                setupGenresRecyclerView()
            }
        }
        movieListViewModel.actorListLiveData.observe(this) { actors ->
            actors?.let {
                setupActorsRecyclerView()
            }
        }
    }

    private fun displayMovieDetails(movie: Movie) {
        with(binding) {
            movieTitle.text = movie.title
            movieDescription.text = movie.description
            movieYear.text = movie.year.toString()
            movieRuntime.text = getString(R.string.movie_runtime_format, movie.runtime)
            movieRating.text = movie.rating.toString()
            movieVotes.text = movie.votes.toString()
            movieRevenue.text = getString(R.string.movie_revenue_format, movie.revenue)
            movieDirector.text = movie.director
        }
        setupGenresRecyclerView()
        setupActorsRecyclerView()
    }

    private fun setupGenresRecyclerView() {
        val movieGenres = movieListViewModel.genreListLiveData.value
            ?.filter { it.id in movieListViewModel.movieLiveData.value?.genres.orEmpty() }
            ?: emptyList()
        binding.genresRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ViewMovieActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = GenreAdapter(movieGenres) {
                // Handle genre click here
            }
        }
    }

    private fun setupActorsRecyclerView() {
        val movieActors = movieListViewModel.actorListLiveData.value
            ?.filter { it.id in movieListViewModel.movieLiveData.value?.actors.orEmpty() }
            ?: emptyList()
        binding.actorsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ViewMovieActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = ActorAdapter(movieActors) {
                // Handle actor click here
            }
        }
    }
}
