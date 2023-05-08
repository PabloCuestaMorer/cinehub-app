package es.usj.pcuestam.cinehubapp.beans

data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val year: Int,
    val runtime: Int,
    val rating: Float,
    val votes: Int,
    val revenue: Float,
    val director: String,
    val genres: List<Int>,
    val actors: List<Int>
)
