package es.usj.pcuestam.cinehubapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import es.usj.pcuestam.cinehubapp.viewmodels.MovieListViewModel
import es.usj.pcuestam.cinehubapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreenActivity : AppCompatActivity() {
    // private val movieRepository = MovieRepository()
    private lateinit var movieListViewModel: MovieListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        movieListViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(MovieListViewModel::class.java)

        loadDataAndNavigate()
    }

    private fun loadDataAndNavigate() {
        CoroutineScope(Dispatchers.IO).launch {
            //movieRepository.loadMoviesFromApi() // Replace with your actual loading function
            movieListViewModel.loadMovieList() // Load movie data
            simulateLoadingData()
            //navigateToMovieListActivity()
        }
    }

    private suspend fun simulateLoadingData() {
        delay(2000) // 2 seconds delay to simulate data loading
        withContext(Dispatchers.Main) {
            navigateToMovieListActivity()
        }
    }

    private fun navigateToMovieListActivity() {
        startActivity(Intent(this, MovieListActivity::class.java))
        finish()
    }
}
