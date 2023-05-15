package es.usj.pcuestam.cinehubapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import es.usj.pcuestam.cinehubapp.adapters.SimpleMovieAdapter
import es.usj.pcuestam.cinehubapp.beans.Genre
import es.usj.pcuestam.cinehubapp.beans.Movie
import es.usj.pcuestam.cinehubapp.databinding.ActivityGenreBinding
import es.usj.pcuestam.cinehubapp.viewmodels.MovieListViewModel

class GenreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenreBinding
    private lateinit var movieListViewModel: MovieListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        fetchGenreDetails()
    }

    private fun initViewModel() {
        movieListViewModel = ViewModelProvider(this)[MovieListViewModel::class.java]
    }

    private fun fetchGenreDetails() {
        val genreId = intent.getIntExtra("GENRE_ID", -1)
        if (genreId != -1) {
            movieListViewModel.fetchGenreById(genreId)
            movieListViewModel.genreLiveData.observe(this) { genre ->
                genre?.let {
                    displayGenreDetails(it)
                }
            }

            // Fetch movies of the genre
            movieListViewModel.fetchMoviesOfTheGenre(genreId)
            movieListViewModel.moviesOfTheGenreLiveData.observe(this) { movies ->
                movies?.let {
                    displayMoviesOfTheGenre(it)
                }
            }
        }
    }

    private fun displayGenreDetails(actor: Genre) {
        binding.genreName.text = actor.name
        // TODO: Load other Genre details
    }

    private fun displayMoviesOfTheGenre(movies: List<Movie>) {
        binding.moviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@GenreActivity)
            adapter = SimpleMovieAdapter(movies)
        }
    }
}