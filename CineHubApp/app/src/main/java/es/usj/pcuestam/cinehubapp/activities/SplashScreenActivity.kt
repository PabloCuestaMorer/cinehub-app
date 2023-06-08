package es.usj.pcuestam.cinehubapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import es.usj.pcuestam.cinehubapp.viewmodels.MovieListViewModel
import es.usj.pcuestam.cinehubapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var movieListViewModel: MovieListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        movieListViewModel = ViewModelProvider(this).get(MovieListViewModel::class.java)

        loadDataAndNavigate()
    }

    private fun loadDataAndNavigate() {
        lifecycleScope.launch {
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
