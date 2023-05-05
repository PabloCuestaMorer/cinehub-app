package es.usj.pcuestam.cinehubapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.usj.pcuestam.cinehubapp.R
import es.usj.pcuestam.cinehubapp.beans.Movie

class MovieListAdapter(

    private var movieList: List<Movie>,
    private val onMovieClickListener: (Movie) -> Unit,
    private val onEditButtonClickListener: (Movie) -> Unit
) : RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>(), Filterable {

    private var movieListFiltered: List<Movie> = movieList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_list_item, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieListFiltered[position]
        holder.bind(movie, onMovieClickListener, onEditButtonClickListener)
    }

    override fun getItemCount(): Int {
        return movieListFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterString = constraint.toString().lowercase().trim()
                movieListFiltered = if (filterString.isEmpty()) {
                    movieList
                } else {
                    movieList.filter { movie ->
                        movie.title.lowercase().contains(filterString)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = movieListFiltered
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                movieListFiltered = results?.values as List<Movie>
                notifyDataSetChanged()
            }
        }
    }

    fun applyFilters(year: Int?, rating: Float?) {
        val filteredList = movieList.filter { movie ->
            val yearMatch = year == null || movie.year == year
            val ratingMatch = rating == null || movie.rating >= rating
            yearMatch && ratingMatch
        }
        movieListFiltered = filteredList
        notifyDataSetChanged()
    }


    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.movie_title)

        fun bind(
            movie: Movie,
            onMovieClickListener: (Movie) -> Unit,
            onEditButtonClickListener: (Movie) -> Unit
        ) {
            titleTextView.text = movie.title
            itemView.setOnClickListener { onMovieClickListener(movie) }

            val editButton: ImageButton = itemView.findViewById(R.id.edit_button)
            editButton.setOnClickListener { onEditButtonClickListener(movie) }
        }
    }
}