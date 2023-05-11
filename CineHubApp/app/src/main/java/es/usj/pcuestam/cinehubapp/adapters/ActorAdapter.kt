package es.usj.pcuestam.cinehubapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.usj.pcuestam.cinehubapp.R
import es.usj.pcuestam.cinehubapp.beans.Actor

class ActorAdapter(
    private val actors: List<Actor>,
    private val onActorClick: (actor: Actor) -> Unit
) : RecyclerView.Adapter<ActorAdapter.ActorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actor, parent, false)
        return ActorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(actors[position], onActorClick)
    }

    override fun getItemCount(): Int = actors.size

    class ActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val actorName: TextView = itemView.findViewById(R.id.actorName)

        fun bind(actor: Actor, onActorClick: (actor: Actor) -> Unit) {
            actorName.text = actor.name
            itemView.setOnClickListener { onActorClick(actor) }
        }
    }
}
