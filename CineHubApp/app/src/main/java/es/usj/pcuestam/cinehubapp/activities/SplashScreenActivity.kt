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

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var movieListViewModel: MovieListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        movieListViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(MovieListViewModel::class.java)

        loadDataAndNavigate()
    }

    private fun loadDataAndNavigate() {
        CoroutineScope(Dispatchers.Main).launch {
            movieListViewModel.loadMovieList()
            delay(2000)
            navigateToMovieListActivity()
        }
    }

    private fun navigateToMovieListActivity() {
        startActivity(Intent(this, MovieListActivity::class.java))
        finish()
    }
}
