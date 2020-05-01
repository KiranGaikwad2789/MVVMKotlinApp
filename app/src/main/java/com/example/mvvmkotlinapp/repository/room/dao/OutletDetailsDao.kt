package com.example.mvvmkotlinapp.repository.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.repository.room.tables.OutletDetails
import com.example.mvvmkotlinapp.repository.room.tables.Product

@Dao
interface OutletDetailsDao {

    @Query("SELECT * FROM OutletDetails")
    fun getOutletDetails(): LiveData<OutletDetails>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOutletDetails(vararg outletDetails: OutletDetails)

    @Query("DELETE FROM OutletDetails")
    fun deleteOutletDetailsTable()

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateOutletDetailsTable(outletDetails: OutletDetails)
}