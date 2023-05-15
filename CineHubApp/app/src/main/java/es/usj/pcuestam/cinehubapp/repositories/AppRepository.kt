package es.usj.pcuestam.cinehubapp.repositories

import es.usj.pcuestam.cinehubapp.api.ApiClient
import es.usj.pcuestam.cinehubapp.beans.Actor
import es.usj.pcuestam.cinehubapp.beans.Genre
import es.usj.pcuestam.cinehubapp.beans.Movie

class AppRepository {
    private val apiService = ApiClient.instance
    suspend fun getAllMovies(): List<Movie>? {
        val response = apiService.getAllMovies()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun getMovieById(movieId: Int): Movie? {
        val response = apiService.getMovieById(movieId)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun addMovie(movie: Movie): Movie? {
        val response = apiService.addMovie(movie)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }


    suspend fun getAllActors(): List<Actor>? {
        val response = apiService.getAllActors()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun getActorById(actorId: Int): Actor? {
        val response = apiService.getActorById(actorId)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun getAllGenres(): List<Genre>? {
        val response = apiService.getAllGenres()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun getGenreById(genreId: Int): Genre? {
        val response = apiService.getGenreById(genreId)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

}
