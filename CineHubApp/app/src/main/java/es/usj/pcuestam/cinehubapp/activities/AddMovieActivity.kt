package es.usj.pcuestam.cinehubapp.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import es.usj.pcuestam.cinehubapp.R
import es.usj.pcuestam.cinehubapp.beans.Actor
import es.usj.pcuestam.cinehubapp.beans.Genre
import es.usj.pcuestam.cinehubapp.viewmodels.MovieListViewModel

class AddMovieActivity : AppCompatActivity() {

    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var genreSpinnerAdapter: ArrayAdapter<String>
    private lateinit var actorSpinnerAdapter: ArrayAdapter<String>
    private lateinit var addActorButton: Button
    private lateinit var addGenreButton: Button
    private lateinit var genreSpinnersContainer: LinearLayout
    private lateinit var actorSpinnersContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        movieListViewModel = ViewModelProvider(this)[MovieListViewModel::class.java]

        addActorButton = findViewById(R.id.add_actor_button)
        addGenreButton = findViewById(R.id.add_genre_button)
        genreSpinnersContainer = findViewById(R.id.genre_spinners_container)
        actorSpinnersContainer = findViewById(R.id.actor_spinners_container)

        movieListViewModel.loadGenresAndActors()
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

        addActorButton.setOnClickListener {
            val spinner = createSpinner()
            spinner.adapter = actorSpinnerAdapter
            actorSpinnersContainer.addView(spinner)
        }

        addGenreButton.setOnClickListener {
            val spinner = createSpinner()
            spinner.adapter = genreSpinnerAdapter
            genreSpinnersContainer.addView(spinner)
        }
    }

    private fun setUpGenreSpinnerAdapter(genres: List<Genre>) {
        genreSpinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            genres.map { it.name }
        )
        genreSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    private fun setUpActorSpinnerAdapter(actors: List<Actor>) {
        actorSpinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            actors.map { it.name }
        )
        actorSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    private fun createSpinner(): Spinner {
        return Spinner(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
    }

}
