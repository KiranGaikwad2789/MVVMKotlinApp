package com.example.mvvmkotlinapp.repository.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvvmkotlinapp.repository.room.tables.OutletDetails

@Dao
interface OutletDetailsDao {

    @Query("SELECT * FROM OutletDetails WHERE outlet_id =:outletId LIMIT 1")
    fun getOutletDetails(outletId: String): LiveData<OutletDetails>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOutletDetails(vararg outletDetails: OutletDetails)

    @Query("DELETE FROM OutletDetails")
    fun deleteOutletDetailsTable()

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateOutletDetailsTable(outletDetails: OutletDetails)
}