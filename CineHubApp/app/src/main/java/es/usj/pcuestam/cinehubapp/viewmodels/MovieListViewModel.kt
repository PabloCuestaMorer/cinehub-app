package es.usj.pcuestam.cinehubapp.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import es.usj.pcuestam.cinehubapp.beans.Movie
import es.usj.pcuestam.cinehubapp.repositories.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieListViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository = AppRepository()

    private val _movieListLiveData: MutableLiveData<List<Movie>> = MutableLiveData()

    // To expose public read-only version of the LiveData
    val movieListLiveData: LiveData<List<Movie>> = _movieListLiveData

    fun loadMovieList() {
        CoroutineScope(Dispatchers.IO).launch {
            val movieList = appRepository.getMovies()
            if (movieList != null) {
                withContext(Dispatchers.Main) {
                    _movieListLiveData.value = movieList
                }
            } else {
                // Handle error or show a message to the user
            }
        }
    }
}
