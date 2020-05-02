package com.example.mvvmkotlinapp.repository.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvvmkotlinapp.repository.room.Route
import com.example.mvvmkotlinapp.repository.room.StartDutyStatus
import com.example.mvvmkotlinapp.repository.room.User

@Dao
interface RouteDao {

    @Query("SELECT * FROM Route")
    fun getAllRoutes(): LiveData<List<Route>>

    @Query("SELECT * FROM Route")
    fun getAllRoutesList(): List<Route>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllRoutes(vararg route: Route)

    @Query("DELETE FROM Route")
    fun deleteRouteTable()

}