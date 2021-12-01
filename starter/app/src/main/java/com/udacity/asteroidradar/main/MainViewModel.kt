package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.data.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    private var _menuOption = MutableLiveData<MainFragment.MenuOption>()
    private val menuOption: LiveData<MainFragment.MenuOption>
        get() = _menuOption

    private val _navigateToDetails = MutableLiveData<Asteroid>()
    val navigateToDetails
        get() = _navigateToDetails

    val asteroids = Transformations.switchMap(menuOption) {
        when (it!!) {
            MainFragment.MenuOption.WEEK -> asteroidRepository.getAsteroids(MainFragment.MenuOption.WEEK)
            MainFragment.MenuOption.SAVED -> asteroidRepository.getAsteroids(MainFragment.MenuOption.SAVED)
            MainFragment.MenuOption.TODAY -> asteroidRepository.getAsteroids(MainFragment.MenuOption.TODAY)
        }
    }

    val pictureOfDay = asteroidRepository.pictureOfTheDay

    init {
        _menuOption.value = MainFragment.MenuOption.WEEK
        refreshData()
    }

    private fun refreshData() {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
            asteroidRepository.getPictureOfTheDay()
        }
    }

    fun onItemClicked(asteroid: Asteroid) {
        Log.d("TEST", "ITEM CLICKED")
        _navigateToDetails.value = asteroid
    }

    fun onItemClickedComplete() {
        _navigateToDetails.value = null
    }

    class ViewModelFactory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    fun updateList(option: MainFragment.MenuOption) {
        _menuOption.value = option
    }
}