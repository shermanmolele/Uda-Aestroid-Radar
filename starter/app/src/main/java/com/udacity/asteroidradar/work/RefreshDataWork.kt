package com.udacity.asteroidradar.work

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.WebService
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.PictureDayDatabase
import com.udacity.asteroidradar.repositories.NASARepository
import kotlinx.coroutines.CoroutineScope
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class RefreshDataWorker(appContext : Context, params : WorkerParameters) : CoroutineWorker(appContext, params){

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
    override suspend fun doWork(): Result {
       lateinit var viewmodelscope : CoroutineScope
        val repository: NASARepository =
            NASARepository(
                // Webservice
                Retrofit.Builder().baseUrl(Constants.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
                    .create(WebService::class.java),
                // Roomdb DAO
                AsteroidDatabase.getInstance(applicationContext)
                    .asteroidDao(),
                Room.databaseBuilder(
                    applicationContext,
                    PictureDayDatabase::class.java,
                    "pic-day-db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .pictureDayDatabase(),
                    viewmodelscope
            )

        return try {
            repository.getAsteroidFeed()
            Result.success()
        } catch (exception: HttpException){
            Result.retry()
        }

    }

}