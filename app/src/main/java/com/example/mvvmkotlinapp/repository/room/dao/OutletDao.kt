package com.example.mvvmkotlinapp.repository.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvvmkotlinapp.repository.room.Outlet

@Dao
interface OutletDao {

    @Query("SELECT * FROM Outlet")
    fun getAllOutlets():LiveData<List<Outlet>>


    @Query("SELECT * FROM Outlet WHERE route_id LIKE :spinnerValueID")
    fun getOutletListFromRouteID(spinnerValueID: String):List<Outlet>

    @Query("SELECT * FROM Outlet WHERE dist_id LIKE :spinnerValueID")
    fun getOutletListFromDistID(spinnerValueID: String):List<Outlet>


    @Query("SELECT outlet_name FROM Outlet WHERE outlet_id LIKE :outeletId")
    fun getOutletNameFromID(outeletId:String):LiveData<String>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllOutlets(vararg outlet: Outlet)

    @Query("DELETE FROM Outlet")
    fun deleteOutletTable()

}