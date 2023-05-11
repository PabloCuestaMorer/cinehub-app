package es.usj.pcuestam.cinehubapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.usj.pcuestam.cinehubapp.R
import es.usj.pcuestam.cinehubapp.beans.Genre

class GenreAdapter(
    private val genres: List<Genre>,
    private val onGenreClick: (genre: Genre) -> Unit
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_genre, parent, false)
        return GenreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(genres[position], onGenreClick)
    }

    override fun getItemCount(): Int = genres.size

    class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val genreName: TextView = itemView.findViewById(R.id.genreName)

        fun bind(genre: Genre, onGenreClick: (genre: Genre) -> Unit) {
            genreName.text = genre.name
            itemView.setOnClickListener { onGenreClick(genre) }
        }
    }
}
