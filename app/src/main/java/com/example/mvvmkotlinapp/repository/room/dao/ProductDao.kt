package com.example.mvvmkotlinapp.repository.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvmkotlinapp.repository.room.tables.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    fun getAllProducts(): LiveData<List<Product>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllProducts(vararg category: Product)

    @Query("DELETE FROM Product")
    fun deleteProductTable()

    @Query("UPDATE Product SET product_isSelected=:product_isSelected,product_quantity=:product_quantity WHERE product_id =:product_id")
    fun updateProductSelectedQuantity(
        product_id: Int?,
        product_isSelected: Boolean?,
        product_quantity: Int?
    )
}