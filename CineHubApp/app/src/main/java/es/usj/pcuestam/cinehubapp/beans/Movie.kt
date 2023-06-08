package es.usj.pcuestam.cinehubapp.beans

import java.io.Serializable

class Movie(
    var id: Int,
    var title: String,
    var description: String,
    var director: String,
    var rating: Double,
    var revenue: Double,
    var runtime: Int,
    var votes: Int,
    var year: String,
    var genres: List<Int> = mutableListOf(),
    var actors: List<Int> = mutableListOf()
) : Serializable
