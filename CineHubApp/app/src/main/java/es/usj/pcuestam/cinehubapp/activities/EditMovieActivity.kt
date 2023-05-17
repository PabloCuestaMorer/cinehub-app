package es.usj.pcuestam.cinehubapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import es.usj.pcuestam.cinehubapp.adapters.ActorArrayAdapter
import es.usj.pcuestam.cinehubapp.adapters.GenreArrayAdapter
import es.usj.pcuestam.cinehubapp.beans.Actor
import es.usj.pcuestam.cinehubapp.beans.Genre
import es.usj.pcuestam.cinehubapp.beans.Movie
import es.usj.pcuestam.cinehubapp.databinding.ActivityEditMovieBinding
import es.usj.pcuestam.cinehubapp.viewmodels.MovieListViewModel

class EditMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditMovieBinding
    private var movieId: Int = 0
    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var actorSpinnerAdapter: ArrayAdapter<Actor>
    private lateinit var genreSpinnerAdapter: ArrayAdapter<Genre>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        setupMovieDetails()
        observeData()
        setupButtonClickListeners()
    }

    private fun initViewModel() {
        movieListViewModel = ViewModelProvider(this)[MovieListViewModel::class.java]
    }

    private fun setupMovieDetails() {
        movieId = intent.getIntExtra("MOVIE_ID", 0)
        if (movieId > 0) {
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
            setupGenresAndActors()
        } else {
            Toast.makeText(this, "Error: Movie ID not found.", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setupGenresAndActors() {
        movieListViewModel.fetchGenresAndActors()
        movieListViewModel.genreListLiveData.observe(this) { genres ->
            movieListViewModel.movieLiveData.observe(this) { movie ->
                if (movie != null && genres != null) {
                    setUpGenreSpinner(genres, movie.genres)
                }
            }
        }

        movieListViewModel.actorListLiveData.observe(this) { actors ->
            movieListViewModel.movieLiveData.observe(this) { movie ->
                if (movie != null && actors != null) {
                    setUpActorSpinner(actors, movie.actors)
                }
            }
        }
    }

    private fun populateFormWithMovieDetails(movie: Movie) {
        binding.apply {
            idEt.text = movie.id.toString()
            titleEt.setText(movie.title)
            descriptionEt.setText(movie.description)
            releaseYearEt.setText(movie.year.toString())
            runtimeEt.setText(movie.runtime.toString())
            ratingEt.setText(movie.rating.toString())
            votesEt.setText(movie.votes.toString())
            revenueEt.setText(movie.revenue.toString())
            movieDirectorEditText.setText(movie.director)
        }
    }

    private fun setUpGenreSpinner(genres: List<Genre>, movieGenresIds: List<Int>) {
        val genreAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            genres.map { it.name }
        )
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.genreSpinnersContainer.removeAllViews()

        movieGenresIds.forEach { movieGenreId ->
            val spinner = createGenreSpinner()
            spinner.adapter = genreAdapter

            val actorPosition = genres.indexOfFirst { it.id == movieGenreId }
            if (actorPosition != -1) {
                spinner.setSelection(actorPosition)
            }

            binding.genreSpinnersContainer.addView(spinner)
        }
    }

    private fun createGenreSpinner() = createSpinner()

    private fun setUpActorSpinner(actors: List<Actor>, movieActorIds: List<Int>) {
        val actorAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            actors.map { it.name }
        )
        actorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.actorSpinnersContainer.removeAllViews()

        movieActorIds.forEach { movieActorId ->
            val spinner = createActorSpinner()
            spinner.adapter = actorAdapter

            val actorPosition = actors.indexOfFirst { it.id == movieActorId }
            if (actorPosition != -1) {
                spinner.setSelection(actorPosition)
            }

            binding.actorSpinnersContainer.addView(spinner)
        }
    }

    private fun createActorSpinner() = createSpinner()

    private fun createSpinner(): Spinner {
        val spinner = Spinner(this)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 8, 0, 8)
        spinner.layoutParams = layoutParams
        return spinner
    }

    private fun observeData() {
        movieListViewModel.genreListLiveData.observe(this) { genres ->
            if (genres != null) {
                setUpGenreSpinnerAdapter(genres)
            }
        }

        movieListViewModel.actorListLiveData.observe(this) { actors ->
            if (actors != null) {
                setUpActorSpinnerAdapter(actors)
            }
        }
    }

    private fun setUpGenreSpinnerAdapter(genres: List<Genre>) {
        genreSpinnerAdapter = GenreArrayAdapter(this, genres)
    }

    private fun setUpActorSpinnerAdapter(actors: List<Actor>) {
        actorSpinnerAdapter = ActorArrayAdapter(this, actors)
    }

    private fun setupButtonClickListeners() {
        binding.addActorButton.setOnClickListener {
            val spinner = createSpinner()
            spinner.adapter = actorSpinnerAdapter
            binding.actorSpinnersContainer.addView(spinner)
        }
        binding.removeActorButton.setOnClickListener {
            removeLastSpinner(binding.actorSpinnersContainer)
        }

        binding.addGenreButton.setOnClickListener {
            val spinner = createSpinner()
            spinner.adapter = genreSpinnerAdapter
            binding.genreSpinnersContainer.addView(spinner)
        }
        binding.removeGenreButton.setOnClickListener {
            removeLastSpinner(binding.genreSpinnersContainer)
        }

    }

    private fun removeLastSpinner(container: LinearLayout) {
        if (container.childCount > 0) {
            container.removeViewAt(container.childCount - 1)
        }
    }
}
