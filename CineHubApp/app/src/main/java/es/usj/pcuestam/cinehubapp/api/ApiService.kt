package es.usj.pcuestam.cinehubapp.api

import es.usj.pcuestam.cinehubapp.beans.Actor
import es.usj.pcuestam.cinehubapp.beans.Genre
import es.usj.pcuestam.cinehubapp.beans.Movie
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/movies")
    suspend fun getAllMovies(): Response<List<Movie>>

    @GET("/movies/{id}")
    suspend fun getMovieById(@Path("id") movieId: Int): Response<Movie>

    @POST("/movies")
    suspend fun addMovie(@Body movie: Movie): Response<Movie>

    @GET("/actors")
    suspend fun getAllActors(): Response<List<Actor>>

    @GET("/actors/{id}")
    suspend fun getActorById(@Path("id") actorId: Int): Response<Actor>

    @GET("/genres")
    suspend fun getAllGenres(): Response<List<Genre>>

    @GET("/genres/{id}")
    suspend fun getGenreById(@Path("id") genreId: Int): Response<Genre>
}
