package com.example.mvvmkotlinapp.repository.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvmkotlinapp.repository.room.tables.Product
import com.example.mvvmkotlinapp.repository.room.tables.ProductCategory

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product WHERE prod_cat_id LIKE :prodCatId AND route_id LIKE :routeId AND outlet_id LIKE :outletId AND dist_id LIKE :distId")
    fun getAllProducts(prodCatId:Int?,routeId:Int?,outletId:Int?,distId:Int?): LiveData<List<Product>>


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