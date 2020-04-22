package com.example.mvvmkotlinapp.repository

import android.app.Application
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.City
import com.example.mvvmkotlinapp.repository.room.CityDao
import com.example.mvvmkotlinapp.repository.room.FeatureDao
import com.example.mvvmkotlinapp.repository.room.dao.ProductCategoryDao
import com.example.mvvmkotlinapp.repository.room.dao.ProductDao
import com.example.mvvmkotlinapp.repository.room.dao.ProductOrderDao
import org.jetbrains.anko.doAsync

class ProductListRepository (application: Application){

    private var productCategoryDao: ProductCategoryDao? = null
    private var productDao: ProductDao? = null
    private var productOrderDao: ProductOrderDao? = null


    var database: AppDatabase? =null

    init {
        database = AppDatabase.getDatabase(application)
        productCategoryDao = database?.productCatDao()
        productDao = database?.productDao()
        productOrderDao = database?.productOrderDao()
    }

    fun getProductCatList() = productCategoryDao?.getAllProductCategory()

    fun getProductList() = productDao?.getAllProducts()

    // Product
    fun deleteProductOrderTable(){
        doAsync {
            productOrderDao!!.deleteProductTable()
        }
    }

    fun insertProductTable(arrayListOrder: ArrayList<ProductOrderModel>) {
        doAsync {
            if (arrayListOrder != null) {
                for (product in arrayListOrder!!) {
                    productOrderDao!!.insertAllProductsOrders(product)
                }
            }
        }
    }
}