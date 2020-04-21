package com.example.mvvmkotlinapp.repository.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvvmkotlinapp.repository.room.Distributor
import com.example.mvvmkotlinapp.repository.room.Outlet

@Dao
interface DistributorDao {

    @Query("SELECT * FROM Distributor")
    fun getAllDistributors(): LiveData<List<Distributor>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllDist(vararg dist: Distributor)

    @Query("DELETE FROM Distributor")
    fun deleteDistTable()
}