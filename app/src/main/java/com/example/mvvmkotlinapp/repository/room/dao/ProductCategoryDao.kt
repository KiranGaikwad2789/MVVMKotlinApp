package com.example.mvvmkotlinapp.repository.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvmkotlinapp.repository.room.City
import com.example.mvvmkotlinapp.repository.room.tables.ProductCategory

@Dao
interface ProductCategoryDao {

    @Query("SELECT * FROM ProductCategory WHERE route_id LIKE :routeId AND outlet_id LIKE :outletId AND dist_id LIKE :distId")
    fun getAllProductCategory(routeId:Int?,outletId:Int?,distId:Int?): LiveData<List<ProductCategory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllProductCategory(vararg category: ProductCategory)

    @Query("DELETE FROM ProductCategory")
    fun deleteProductCategoryTable()
}