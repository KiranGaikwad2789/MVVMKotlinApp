package com.example.mvvmkotlinapp.repository.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvvmkotlinapp.model.ProductOrderModel


@Dao
interface ProductOrderDao {

    @Query("SELECT * FROM ProductOrderModel WHERE status = 1")
    fun getAllProductOrders(): LiveData<List<ProductOrderModel>>

    @Query("SELECT * FROM ProductOrderModel WHERE master_product_orderid=:masterProductOrderId")
    fun getOrderedProductList(masterProductOrderId:Int): LiveData<List<ProductOrderModel>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllProductsOrders(users: ProductOrderModel):Long


    @Query("DELETE FROM ProductOrderModel")
    fun deleteProductTable()

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateProductCart(productSelected: ProductOrderModel)

    @Query("UPDATE ProductOrderModel SET status=0 WHERE uid = :id")
    fun removeProductCart(id: Int)

    @Query("UPDATE ProductOrderModel SET master_product_orderid=:masterOrderID,status=:status WHERE uid =:productCart")
    fun updateProductMasterProductID(
        productCart: Int,
        masterOrderID: Int?,
        status: String)

    @Query("UPDATE ProductOrderModel SET product_delivered_quantity=:productdeliveredQuantity WHERE uid =:product_id")
    fun updateShortCloseDeliveredQuantity(
        product_id: Int?,
        productdeliveredQuantity: Int?)

}