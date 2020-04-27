package com.example.mvvmkotlinapp.repository.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvvmkotlinapp.repository.room.tables.MasterProductOrder

@Dao
interface MasterProductOrderDao {

    @Query("SELECT * FROM MasterProductOrder WHERE order_status = 'Pending'")
    fun getAllMasterProductOrders(): LiveData<List<MasterProductOrder>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMasterProductsOrders(masterProductOrder: MasterProductOrder):Long


    @Query("DELETE FROM MasterProductOrder")
    fun deleteMasterProductTable()

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateMasterProductCart(productSelected: MasterProductOrder)

    @Query("UPDATE MasterProductOrder SET order_status=0 WHERE uid = :id")
    fun removeMasterProductCart(id: Int)
}