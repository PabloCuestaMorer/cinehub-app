package es.usj.pcuestam.cinehubapp.beans

import org.json.JSONObject

data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val year: Int,
    val rating: Double,
    val poster: String,
    val genres: List<String>,
    val actors: List<String>,
    val directors: List<String>
) {

    companion object {
        fun fromJson(json: JSONObject): Movie {
            val id = json.getInt("id")
            val title = json.getString("title")
            val description = json.getString("description")
            val year = json.getInt("year")
            val rating = json.getDouble("rating")
            val poster = json.getString("poster")
            val genresJsonArray = json.getJSONArray("genres")
            val genresList = (0 until genresJsonArray.length()).map { genresJsonArray.getString(it) }
            val actorsJsonArray = json.getJSONArray("actors")
            val actorsList = (0 until actorsJsonArray.length()).map { actorsJsonArray.getString(it) }
            val directorsJsonArray = json.getJSONArray("directors")
            val directorsList = (0 until directorsJsonArray.length()).map { directorsJsonArray.getString(it) }


            return Movie(id, title, description, year, rating, poster, genresList, actorsList, directorsList)
        }
    }
}
