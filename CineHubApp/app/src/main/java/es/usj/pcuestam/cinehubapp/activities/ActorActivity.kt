package es.usj.pcuestam.cinehubapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import es.usj.pcuestam.cinehubapp.adapters.MovieListAdapter
import es.usj.pcuestam.cinehubapp.adapters.SimpleMovieAdapter
import es.usj.pcuestam.cinehubapp.beans.Movie
import es.usj.pcuestam.cinehubapp.databinding.ActivityActorBinding
import es.usj.pcuestam.cinehubapp.viewmodels.MovieListViewModel

class ActorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActorBinding
    private lateinit var movieListViewModel: MovieListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        fetchActorDetails()
    }

    private fun initViewModel() {
        movieListViewModel = ViewModelProvider(this)[MovieListViewModel::class.java]
    }

    private fun fetchActorDetails() {
        val actorId = intent.getIntExtra("ACTOR_ID", -1)
        if (actorId != -1) {
            // TODO: Fetch actor details and display them

            // Fetch movies that the actor has participated in and display them
            movieListViewModel.fetchMoviesWithActor(actorId)
            movieListViewModel.moviesWithActorLiveData.observe(this) { movies ->
                movies?.let {
                    displayMoviesWithActor(it)
                }
            }
        }
    }

    private fun displayMoviesWithActor(movies: List<Movie>) {
        binding.moviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ActorActivity)
            adapter = SimpleMovieAdapter(movies)
        }
    }


}
