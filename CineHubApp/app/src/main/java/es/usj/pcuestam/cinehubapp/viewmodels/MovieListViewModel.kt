package es.usj.pcuestam.cinehubapp.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import es.usj.pcuestam.cinehubapp.beans.Actor
import es.usj.pcuestam.cinehubapp.beans.Genre
import es.usj.pcuestam.cinehubapp.beans.Movie
import es.usj.pcuestam.cinehubapp.repositories.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieListViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository = AppRepository()

    private val _movieListLiveData: MutableLiveData<List<Movie>> = MutableLiveData()
    val movieListLiveData: LiveData<List<Movie>> = _movieListLiveData
    private val _movieLiveData: MutableLiveData<Movie> = MutableLiveData()
    val movieLiveData: LiveData<Movie> = _movieLiveData

    private val _genreListLiveData: MutableLiveData<List<Genre>> = MutableLiveData()
    val genreListLiveData: LiveData<List<Genre>> = _genreListLiveData
    private val _actorListLiveData: MutableLiveData<List<Actor>> = MutableLiveData()
    val actorListLiveData: LiveData<List<Actor>> = _actorListLiveData

    fun loadMovieList(): Boolean {
        var dataLoaded = false
        CoroutineScope(Dispatchers.IO).launch {
            val movieList = appRepository.getAllMovies()
            if (movieList != null) {
                withContext(Dispatchers.Main) {
                    _movieListLiveData.value = movieList
                    dataLoaded = true
                }
            } else {
                // Handle error or show a message to the user
            }
            loadGenresAndActors()
        }
        return dataLoaded
    }

    // Fetch movie, genres, and actors:
    fun fetchMovieById(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val movie = appRepository.getMovieById(movieId)
            if (movie != null) {
                withContext(Dispatchers.Main) {
                    _movieLiveData.value = movie
                }
            } else {
                // Handle error or show a message to the user
            }
        }
    }

    fun loadGenresAndActors() {
        CoroutineScope(Dispatchers.IO).launch {
            val genres = appRepository.getAllGenres()
            val actors = appRepository.getAllActors()

            withContext(Dispatchers.Main) {
                if (genres != null) {
                    _genreListLiveData.value = genres
                }
                if (actors != null) {
                    _actorListLiveData.value = actors
                }
            }
        }
    }

    fun getGenreById(genreId: Int): Genre? {
        return genreListLiveData.value?.find { it.id == genreId }
    }

    fun getActorById(actorId: Int): Actor? {
        return actorListLiveData.value?.find { it.id == actorId }
    }


}
