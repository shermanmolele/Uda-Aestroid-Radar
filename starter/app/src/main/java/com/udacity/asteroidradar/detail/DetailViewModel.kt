package com.udacity.asteroidradar.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.AsteroidDao

class DetailViewModel( private val singleAsteroidKey: Long = 0L,
dataSource: AsteroidDao) : ViewModel() {

    val database = dataSource

    private val singleAsteroid = MediatorLiveData<Asteroid>()

    fun getSingleAsteroid() = singleAsteroid

    init {
        singleAsteroid.addSource(database.getNightWithId(singleAsteroidKey), singleAsteroid::setValue)
    }

    private val _navigateToAsteroidFeed = MutableLiveData<Boolean?>()

    val navigateToAsteroidFeed: LiveData<Boolean?>
        get() = _navigateToAsteroidFeed

    fun doneNavigating() {
       _navigateToAsteroidFeed.value = null
    }

    fun onClose() {
        _navigateToAsteroidFeed.value = true
    }
}