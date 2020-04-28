package com.example.mvvmkotlinapp.repository

import android.app.Application
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.dao.DistributorDao
import com.example.mvvmkotlinapp.repository.room.dao.MasterProductOrderDao
import com.example.mvvmkotlinapp.repository.room.dao.OutletDao
import com.example.mvvmkotlinapp.repository.room.dao.ProductOrderDao

class OrderDeliveryRpository  (application: Application){

    private var database: AppDatabase? =null
    private var masterProductOrderDao: MasterProductOrderDao? = null
    private var outletDao: OutletDao? = null
    private var distributorDao: DistributorDao? = null
    private var productOrderDao: ProductOrderDao? = null

    init {
        database = AppDatabase.getDatabase(application)
        masterProductOrderDao = database?.masterProductOrderDao()
        outletDao = database?.outletDao()
        distributorDao = database?.distributorDao()
        productOrderDao = database?.productOrderDao()
    }

    fun getOrderDeliveryList() = masterProductOrderDao?.getAllMasterProductOrders()

    fun getOutletNameFromID(outeletId:String) = outletDao?.getOutletNameFromID(outeletId)!!

    fun getDistributorNameFromID(distId:String) = distributorDao?.getOutletNameFromID(distId)!!

    fun getOrderedProductList(masterProductOrderId:Int) = productOrderDao?.getOrderedProductList(masterProductOrderId)


}