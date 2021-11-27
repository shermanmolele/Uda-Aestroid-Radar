package com.udacity.asteroidradar.main

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding

/**
 * Asteroid Feed Adapter.
 */
class AsteroidFeedAdapter(private val context: Context?,
                          val clickListener: AestroidListener,
                          private val asteroids: List<Asteroid>,
                          private val itemClick: (pos: Int) -> Unit) : RecyclerView.Adapter<AsteroidViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.from(parent)
    }

    override fun getItemCount() = asteroids.size

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = asteroids[position]
        holder.name.text = asteroid.codename
        holder.date.text = asteroid.closeApproachDate

        holder.icon.isSelected = asteroid.isPotentiallyHazardous

        holder.icon.contentDescription = if (asteroid.isPotentiallyHazardous) {
            context?.getString(R.string.hazard_icon_content_desc)
        } else {
            context?.getString(R.string.non_hazard_icon_content_desc)
        }
        holder.bind(clickListener, asteroid)
    }
}

class AsteroidViewHolder(val binding: AsteroidListItemBinding)
    : RecyclerView.ViewHolder(binding.root) {
    val name: TextView = itemView.findViewById(R.id.asteroid_item_name)
    val date: TextView = itemView.findViewById(R.id.asteroid_item_date)
    val icon: ImageView = itemView.findViewById(R.id.asteroid_item_hazard_icon)

    fun bind(clickListener: AestroidListener, item: Asteroid) {
        binding.astroid = item
        binding.clicklistener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): AsteroidViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AsteroidListItemBinding.inflate(layoutInflater, parent, false)

            return AsteroidViewHolder(binding)
        }
    }
}

class AestroidListener(val clickListener: (aestroidId : Long) -> Unit){
    fun onClick(aestroid: Asteroid) = clickListener(aestroid.id)
}