package com.example.mvvmkotlinapp.repository.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvvmkotlinapp.repository.room.Outlet
import com.example.mvvmkotlinapp.repository.room.Route
import com.example.mvvmkotlinapp.repository.room.User

@Dao
interface OutletDao {

    @Query("SELECT * FROM Outlet")
    fun getAllOutlets():LiveData<List<Outlet>>

    @Query("SELECT * FROM Outlet WHERE route_id LIKE :route_id")
    fun getAllOutlets(route_id: String): List<Outlet>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllOutlets(vararg outlet: Outlet)

    @Query("DELETE FROM Outlet")
    fun deleteOutletTable()

}