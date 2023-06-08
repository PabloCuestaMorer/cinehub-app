package es.usj.pcuestam.cinehubapp.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import es.usj.pcuestam.cinehubapp.R
import es.usj.pcuestam.cinehubapp.beans.Actor
import es.usj.pcuestam.cinehubapp.beans.Genre
import es.usj.pcuestam.cinehubapp.beans.Movie
import es.usj.pcuestam.cinehubapp.viewmodels.MovieListViewModel
import kotlinx.coroutines.launch
import androidx.core.view.children
import es.usj.pcuestam.cinehubapp.adapters.ActorArrayAdapter
import es.usj.pcuestam.cinehubapp.adapters.GenreArrayAdapter
import es.usj.pcuestam.cinehubapp.databinding.ActivityAddMovieBinding
import es.usj.pcuestam.cinehubapp.repositories.AppRepository

class AddMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMovieBinding
    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var addActorButton: Button
    private lateinit var removeActorButton: Button
    private lateinit var genreSpinnersContainer: LinearLayout
    private lateinit var genreSpinnerAdapter: ArrayAdapter<Genre>
    private lateinit var addGenreButton: Button
    private lateinit var removeGenreButton: Button
    private lateinit var actorSpinnersContainer: LinearLayout
    private lateinit var actorSpinnerAdapter: ArrayAdapter<Actor>
    private val appRepository = AppRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initViews()
        observeData()
        setupButtonClickListeners()
    }

    private fun initViewModel() {
        movieListViewModel = ViewModelProvider(this)[MovieListViewModel::class.java]
        movieListViewModel.fetchGenresAndActors()
    }

    private fun initViews() {
        addActorButton = binding.addActorButton
        removeActorButton = binding.removeActorButton
        addGenreButton = binding.addGenreButton
        removeGenreButton = binding.removeGenreButton
        genreSpinnersContainer = binding.genreSpinnersContainer
        actorSpinnersContainer = binding.actorSpinnersContainer
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

        binding.saveButton.setOnClickListener {
            val newMovie = createNewMovieFromInputs()
            addNewMovie(newMovie)
        }
    }

    private fun createSpinner(): Spinner {
        return Spinner(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun removeLastSpinner(container: LinearLayout) {
        if (container.childCount > 0) {
            container.removeViewAt(container.childCount - 1)
        }
    }

    private fun addNewMovie(newMovie: Movie) {
        lifecycleScope.launch {
            val addedMovie = appRepository.addMovie(newMovie)
            if (addedMovie != null) {
                Toast.makeText(
                    this@AddMovieActivity, "Movie added successfully", Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                Toast.makeText(this@AddMovieActivity, "Failed to add movie", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun createNewMovieFromInputs(): Movie {
        val title = findViewById<EditText>(R.id.title_et).text.toString()
        val description = findViewById<EditText>(R.id.description_et).text.toString()
        val year = findViewById<EditText>(R.id.release_year_et).text.toString()
        val runtime = findViewById<EditText>(R.id.runtime_et).text.toString().toIntOrNull() ?: 0
        val rating = findViewById<EditText>(R.id.rating_et).text.toString().toDoubleOrNull() ?: 0.0
        val votes = findViewById<EditText>(R.id.votes_et).text.toString().toIntOrNull() ?: 0
        val revenue =
            findViewById<EditText>(R.id.revenue_et).text.toString().toDoubleOrNull() ?: 0.0
        val director = findViewById<EditText>(R.id.movie_director_edit_text).text.toString()
        val actors = getSelectedActorIds()
        val genres = getSelectedGenreIds()

        return Movie(
            0, title, description, director, rating, revenue, runtime, votes, year, genres, actors
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


