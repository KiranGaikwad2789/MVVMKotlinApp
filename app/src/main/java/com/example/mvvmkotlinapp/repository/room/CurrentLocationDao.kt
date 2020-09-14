package com.example.mvvmkotlinapp.repository.room

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface CurrentLocationDao {

    @Query("SELECT * FROM CurrentLocation")
    fun getAllLocation(): LiveData<List<CurrentLocation>>

    @Query("SELECT * FROM CurrentLocation WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<CurrentLocation>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg currentLocationDao: CurrentLocation)

    @Query("DELETE FROM CurrentLocation")
    fun deleteTableAllEntry()

    @Query("SELECT * FROM CurrentLocation ORDER BY uid DESC LIMIT 1")
    fun getAllLocationList(): CurrentLocation

}