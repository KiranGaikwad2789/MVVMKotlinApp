package com.example.mvvmkotlinapp.repository.room

import androidx.room.*


@Dao
interface CurrentLocationDao {

    @Query("SELECT * FROM CurrentLocation")
    fun getAll(): List<CurrentLocation>

    @Query("SELECT * FROM CurrentLocation WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<CurrentLocation>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg currentLocationDao: CurrentLocation)

    @Delete
    fun delete(user: CurrentLocation)

}