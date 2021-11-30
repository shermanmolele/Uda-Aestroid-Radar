package com.udacity.asteroidradar.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding

/**
 * Asteroid Feed Adapter.
 */
class AsteroidFeedAdapter(private val context: Context?,
                          val clickListener: AestroidListener,
                          private val asteroids: List<Asteroid>) : RecyclerView.Adapter<AsteroidViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.from(parent)
    }

    override fun getItemCount() = asteroids.size

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = asteroids[position]
        holder.bind(clickListener, asteroid)
    }
}

class AsteroidViewHolder(val binding: AsteroidListItemBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: AestroidListener, item: Asteroid) {
        binding.astroid = item
        binding.asteroidItemName.text = item.codename
        binding.asteroidItemDate.text = item.closeApproachDate
        binding.asteroidItemHazardIcon.isSelected = item.isPotentiallyHazardous
        binding.asteroidItemHazardIcon.contentDescription =  if (item.isPotentiallyHazardous)
        {R.string.hazard_icon_content_desc.toString()}
        else {R.string.non_hazard_icon_content_desc.toString() }
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