package es.usj.pcuestam.cinehubapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.usj.pcuestam.cinehubapp.R
import es.usj.pcuestam.cinehubapp.beans.Movie

class SimpleMovieAdapter(private val movieList: List<Movie>) :
    RecyclerView.Adapter<SimpleMovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.simple_movie_list_item, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.simple_movie_title)

        fun bind(movie: Movie) {
            titleTextView.text = movie.title
        }
    }
}
