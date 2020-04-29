package com.example.mvvmkotlinapp.repository

import android.app.Application
import android.util.Log
import androidx.annotation.UiThread
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.repository.room.*
import com.example.mvvmkotlinapp.repository.room.dao.MasterProductOrderDao
import com.example.mvvmkotlinapp.repository.room.dao.ProductCategoryDao
import com.example.mvvmkotlinapp.repository.room.dao.ProductDao
import com.example.mvvmkotlinapp.repository.room.dao.ProductOrderDao
import com.example.mvvmkotlinapp.repository.room.tables.MasterProductOrder
import com.google.android.gms.tasks.Tasks.await
import io.reactivex.Single
import org.jetbrains.anko.doAsync
import java.util.concurrent.Callable

class ProductListRepository (application: Application){

    private var productCategoryDao: ProductCategoryDao? = null
    private var productDao: ProductDao? = null
    private var productOrderDao: ProductOrderDao? = null
    private var masterProductOrderDao: MasterProductOrderDao? = null

    var database: AppDatabase? =null

    init {
        database = AppDatabase.getDatabase(application)
        productCategoryDao = database?.productCatDao()
        productDao = database?.productDao()
        productOrderDao = database?.productOrderDao()
        masterProductOrderDao = database?.masterProductOrderDao()
    }

    fun getProductCatList() = productCategoryDao?.getAllProductCategory()

    fun getProductList() = productDao?.getAllProducts()

    fun getSelectedProductList() = productOrderDao?.getAllProductOrders()

    //fun updateProductCart(productSelected: ProductOrderModel) = productOrderDao?.updateProductCart(productSelected)

    fun updateProductCart(productSelected: ProductOrderModel){
        doAsync {
            productOrderDao?.updateProductCart(productSelected)
        }
    }

    fun updateProductMasterProductID(arryListProductCart: ArrayList<ProductOrderModel>?, masterOrderID: Int?, status: String){

        doAsync {
            for (productCart in arryListProductCart!!){
                Log.e("productCart data: ",""+ productCart)
                productOrderDao?.updateProductMasterProductID(productCart.uid,masterOrderID,status)
            }
        }
    }

    fun updateProductSelectedQuantity(product_id: Int?, product_isSelected: Boolean?, product_quantity: Int?){
        doAsync {
            productDao?.updateProductSelectedQuantity(product_id,product_isSelected,product_quantity)
        }
    }

    fun removeProductCart(productCartId: Int){
        doAsync {
            productOrderDao?.removeProductCart(productCartId)
        }
    }


    // Product
    fun deleteProductOrderTable(){
        doAsync {
            productOrderDao!!.deleteProductTable()
        }
    }

    /*fun insertSelectedProducts(arrayListOrder: ArrayList<ProductOrderModel>): Single<Long>?  {

        doAsync {
            if (arrayListOrder != null) {
                for (product in arrayListOrder) {
                    //insertNewUser(product)
                    Log.e("Product cart add loop: ",""+product.product_name)
                    var result: Single<Long>? =insertNewUser(product)
                    Log.e("Product result: ",""+result.toString())
                }
            }
        }

    }*/

    fun insertNewUser(arrayListOrder: ProductOrderModel): Single<Long>? {
        return Single.fromCallable(
            Callable<Long> { productOrderDao!!.insertAllProductsOrders(arrayListOrder) }
        )
    }

    //fun addNewMasterProductOrder(masterProductOrder: MasterProductOrder) = masterProductOrderDao?.insertMasterProductsOrders(masterProductOrder)!!


    fun addNewMasterProductOrder(masterProductOrder: MasterProductOrder): Single<Long>? {
        return Single.fromCallable(
            Callable<Long> {  masterProductOrderDao?.insertMasterProductsOrders(masterProductOrder) }
        )
    }
/*
    fun addNewMasterProductOrder(masterProductOrder: MasterProductOrder){
        doAsync {
            masterProductOrderDao?.insertMasterProductsOrders(masterProductOrder):Long
        }
    }*/

}