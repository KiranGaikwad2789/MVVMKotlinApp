package com.example.mvvmkotlinapp.repository.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CityDao {

    @Query("SELECT * FROM City")
    fun getCityList(): LiveData<List<City>>

    @Query("SELECT * FROM City WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<City>

    @Query("SELECT * FROM City WHERE cityId LIKE :cityId AND " + "cityName LIKE :cityName LIMIT 1")
    fun findByName(cityId: String, cityName: String): List<City>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg users: City)

    @Delete
    fun delete(user: City)

    @Query("DELETE FROM City")
    fun deleteTableAllEntry()

}