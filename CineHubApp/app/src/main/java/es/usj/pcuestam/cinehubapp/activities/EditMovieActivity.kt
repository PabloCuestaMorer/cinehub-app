package es.usj.pcuestam.cinehubapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import es.usj.pcuestam.cinehubapp.adapters.ActorArrayAdapter
import es.usj.pcuestam.cinehubapp.adapters.GenreArrayAdapter
import es.usj.pcuestam.cinehubapp.beans.Actor
import es.usj.pcuestam.cinehubapp.beans.Genre
import es.usj.pcuestam.cinehubapp.beans.Movie
import es.usj.pcuestam.cinehubapp.databinding.ActivityEditMovieBinding
import es.usj.pcuestam.cinehubapp.repositories.AppRepository
import es.usj.pcuestam.cinehubapp.viewmodels.MovieListViewModel
import kotlinx.coroutines.launch

class EditMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditMovieBinding
    private var movieId: Int = 0
    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var actorSpinnerAdapter: ArrayAdapter<Actor>
    private lateinit var actorSpinnersContainer: LinearLayout
    private lateinit var addActorButton: Button
    private lateinit var removeActorButton: Button
    private lateinit var genreSpinnerAdapter: ArrayAdapter<Genre>
    private lateinit var genreSpinnersContainer: LinearLayout
    private lateinit var addGenreButton: Button
    private lateinit var removeGenreButton: Button
    private val appRepository = AppRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initViews()
        setupMovieDetails()
        observeData()
        setupButtonClickListeners()
    }

    private fun initViewModel() {
        movieListViewModel = ViewModelProvider(this)[MovieListViewModel::class.java]
    }

    private fun initViews() {
        actorSpinnersContainer = binding.actorSpinnersContainer
        genreSpinnersContainer = binding.genreSpinnersContainer
        addActorButton = binding.addActorButton
        removeActorButton = binding.removeActorButton
        addGenreButton = binding.addGenreButton
        removeGenreButton = binding.removeGenreButton
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
            releaseYearEt.setText(movie.year)
            runtimeEt.setText(movie.runtime.toString())
            ratingEt.setText(movie.rating.toString())
            votesEt.setText(movie.votes.toString())
            revenueEt.setText(movie.revenue.toString())
            movieDirectorEditText.setText(movie.director)
        }
    }

    private fun setUpGenreSpinner(genres: List<Genre>, movieGenresIds: List<Int>) {
        val genreAdapter = GenreArrayAdapter(this, genres)
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

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

    private fun createGenreSpinner() = createSpinner()

    private fun setUpActorSpinner(actors: List<Actor>, movieActorIds: List<Int>) {
        val actorAdapter = ActorArrayAdapter(this, actors)
        actorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

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

    private fun createActorSpinner() = createSpinner()

    private fun createSpinner(): Spinner {
        val spinner = Spinner(this)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 8, 0, 8)
        spinner.layoutParams = layoutParams
        return spinner
    }


    private fun removeLastSpinner(container: LinearLayout) {
        if (container.childCount > 0) {
            container.removeViewAt(container.childCount - 1)
        }
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
            actorSpinnersContainer.addView(spinner)
        }
        binding.removeActorButton.setOnClickListener {
            removeLastSpinner(actorSpinnersContainer)
        }

        binding.addGenreButton.setOnClickListener {
            val spinner = createSpinner()
            spinner.adapter = genreSpinnerAdapter
            genreSpinnersContainer.addView(spinner)
        }
        binding.removeGenreButton.setOnClickListener {
            removeLastSpinner(genreSpinnersContainer)
        }
        binding.saveButton.setOnClickListener {
            val editedMovie = createEditedMovieFromInputs()
            editMovie(editedMovie)
        }
    }

    private fun editMovie(movie: Movie) {
        lifecycleScope.launch {
            val success = appRepository.updateMovie(movie)
            if (success) {
                Toast.makeText(
                    this@EditMovieActivity,
                    "Movie ${movie.id} ${movie.title} updated successfully",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                Toast.makeText(
                    this@EditMovieActivity,
                    "Failed to update movie ${movie.id} ${movie.title}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun createEditedMovieFromInputs(): Movie {
        val id = movieId
        val title = binding.titleEt.text.toString()
        val description = binding.descriptionEt.text.toString()
        val year = binding.releaseYearEt.text.toString()
        val runtime = binding.runtimeEt.text.toString().toIntOrNull() ?: 0
        val rating = binding.ratingEt.text.toString().toDoubleOrNull() ?: 0.0
        val votes = binding.votesEt.text.toString().toIntOrNull() ?: 0
        val revenue = binding.revenueEt.text.toString().toDoubleOrNull() ?: 0.0
        val director = binding.movieDirectorEditText.text.toString()
        val actors = getSelectedActorIds()
        val genres = getSelectedGenreIds()

        return Movie(
            id, title, description, director, rating, revenue, runtime, votes, year, genres, actors
        )
    }

    private fun getSelectedActorIds(): List<Int> {
        return actorSpinnersContainer.children.map { view ->
            (view as Spinner).selectedItem as Actor
        }.map { actor ->
            actor.id
        }.toList()

    }

    private fun getSelectedGenreIds(): List<Int> {
        return genreSpinnersContainer.children.map { view ->
            (view as Spinner).selectedItem as Genre
        }.map { genre ->
            genre.id
        }.toList()
    }

}
