package com.example.mvvmkotlinapp.repository.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvmkotlinapp.model.ProductOrderModel

@Dao
interface ProductOrderDao {

    @Query("SELECT * FROM ProductOrderModel")
    fun getAllProductOrders(): LiveData<List<ProductOrderModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllProductsOrders(vararg category: ProductOrderModel)

    @Query("DELETE FROM ProductOrderModel")
    fun deleteProductTable()
}