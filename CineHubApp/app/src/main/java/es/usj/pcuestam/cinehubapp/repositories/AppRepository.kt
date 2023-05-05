package es.usj.pcuestam.cinehubapp.repositories

import es.usj.pcuestam.cinehubapp.api.ApiClient
import es.usj.pcuestam.cinehubapp.beans.Actor
import es.usj.pcuestam.cinehubapp.beans.Genre
import es.usj.pcuestam.cinehubapp.beans.Movie

class AppRepository {

    private val apiService = ApiClient.instance

    suspend fun getMovies(): List<Movie>? {
        val response = apiService.getMovies()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
    /*
        suspend fun getActors(): List<Actor>? {
            val response = apiService.getActors()
            return if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }

        suspend fun getGenres(): List<Genre>? {
            val response = apiService.getGenres()
            return if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
        */
}
