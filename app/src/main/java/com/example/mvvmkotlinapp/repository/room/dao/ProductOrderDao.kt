package com.example.mvvmkotlinapp.repository.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvvmkotlinapp.model.ProductOrderModel


@Dao
interface ProductOrderDao {

    @Query("SELECT * FROM ProductOrderModel WHERE status = 1")
    fun getAllProductOrders(): LiveData<List<ProductOrderModel>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllProductsOrders(users: ProductOrderModel):Long


    @Query("DELETE FROM ProductOrderModel")
    fun deleteProductTable()

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateProductCart(productSelected: ProductOrderModel)

    @Query("UPDATE ProductOrderModel SET status=0 WHERE uid = :id")
    fun removeProductCart(id: Int)

    @Query("UPDATE ProductOrderModel SET master_product_orderid=:masterOrderID WHERE uid = :uid")
    fun updateProductMasterProductID(uid: Int, masterOrderID: Long)

}