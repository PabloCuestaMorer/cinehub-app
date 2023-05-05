package es.usj.pcuestam.cinehubapp.api

import es.usj.pcuestam.cinehubapp.beans.Actor
import es.usj.pcuestam.cinehubapp.beans.Genre
import es.usj.pcuestam.cinehubapp.beans.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    // API endpoints
    @GET("/movies")
    suspend fun getMovies(): Response<List<Movie>>

    // Fetch a single movie by its ID
    @GET("/movies/{id}")
    suspend fun getMovieById(@Path("id") movieId: Int): Response<Movie>

    @GET("/actors")
    suspend fun getAllActors(): Response<List<Actor>>

    @GET("/genres")
    suspend fun getAllGenres(): Response<List<Genre>>
}
