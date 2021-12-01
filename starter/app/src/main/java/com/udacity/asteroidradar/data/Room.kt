package com.udacity.asteroidradar.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {

    @Query("select * from  databasepictureofday")
    fun getTodayPicture(): LiveData<DatabasePictureOfDay>

    @Query("select * from DatabaseAsteroid where date(closeApproachDate) >= date('now') order by date(closeApproachDate) desc ")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseasteroid order by date(closeApproachDate) desc")
    fun getSavedAsteroid(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseasteroid  where date(closeApproachDate)=date('now')")
    fun getTodayAsteroid(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseasteroid  where date(closeApproachDate) between date('now') and date('now','+7 days') order by date(closeApproachDate) desc")
    fun getWeekAsteroid(): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictureOfTheDay(vararg pictureOfDay: DatabasePictureOfDay)

}

@Database(entities = [DatabaseAsteroid::class, DatabasePictureOfDay::class], version = 2)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AsteroidsDatabase::class.java,
                    "asteroids").fallbackToDestructiveMigration()
                    .build()
        }
    }
    return INSTANCE
}
