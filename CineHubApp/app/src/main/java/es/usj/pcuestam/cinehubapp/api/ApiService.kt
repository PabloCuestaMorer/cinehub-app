package es.usj.pcuestam.cinehubapp.api

import es.usj.pcuestam.cinehubapp.beans.Actor
import es.usj.pcuestam.cinehubapp.beans.Genre
import es.usj.pcuestam.cinehubapp.beans.Movie
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    // Add your API endpoints here
    @GET("/movies")
    suspend fun getMovies(): Response<List<Movie>>

    /*
    @GET("actors")
    suspend fun getActors(): Response<List<Actor>>

    @GET("genres")
    suspend fun getGenres(): Response<List<Genre>>
    */
}
