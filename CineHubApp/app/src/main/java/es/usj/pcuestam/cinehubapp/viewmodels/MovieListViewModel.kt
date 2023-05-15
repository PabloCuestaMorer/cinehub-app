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
    private val _genreLiveData = MutableLiveData<Genre>()
    val genreLiveData: LiveData<Genre>
        get() = _genreLiveData


    private val _actorListLiveData: MutableLiveData<List<Actor>> = MutableLiveData()
    val actorListLiveData: LiveData<List<Actor>> = _actorListLiveData
    private val _actorLiveData = MutableLiveData<Actor>()
    val actorLiveData: LiveData<Actor>
        get() = _actorLiveData

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
        }
        return dataLoaded
    }

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

    fun fetchGenresAndActors() {
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

    private val _moviesOfTheActorLiveData: MutableLiveData<List<Movie>> = MutableLiveData()
    val moviesOfTheActorLiveData: LiveData<List<Movie>> = _moviesOfTheActorLiveData

    fun fetchMoviesOfTheActor(actorId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val allMovies = appRepository.getAllMovies()
            val moviesOfTheActor = allMovies?.filter { movie -> movie.actors.contains(actorId) }

            if (moviesOfTheActor != null) {
                withContext(Dispatchers.Main) {
                    _moviesOfTheActorLiveData.value = moviesOfTheActor
                }
            } else {
                // Handle error or show a message to the user
            }
        }
    }

    fun fetchActorById(actorId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val actor = appRepository.getActorById(actorId)
            if (actor != null) {
                withContext(Dispatchers.Main) {
                    _actorLiveData.postValue(actor)
                }
            } else {
                // Handle error or show a message to the user
            }
        }
    }

    private val _moviesOfTheGenreLiveData: MutableLiveData<List<Movie>> = MutableLiveData()
    val moviesOfTheGenreLiveData: MutableLiveData<List<Movie>> = _moviesOfTheGenreLiveData

    fun fetchMoviesOfTheGenre(genreId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val allMovies = appRepository.getAllMovies()
            val moviesOfTheGenre = allMovies?.filter { movie -> movie.genres.contains(genreId) }
            if (moviesOfTheGenre != null) {
                withContext(Dispatchers.Main) {
                    _moviesOfTheGenreLiveData.value = moviesOfTheGenre
                }
            } else {
                // Handle error or show a message to the user
            }
        }
    }

    fun fetchGenreById(genreId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val genre = appRepository.getGenreById(genreId)
            if (genre != null) {
                withContext(Dispatchers.Main) {
                    _genreLiveData.postValue(genre)
                }
            } else {
                // Handle error or show a message to the user
            }
        }
    }

}
