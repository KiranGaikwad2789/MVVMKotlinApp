package com.example.mvvmkotlinapp.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.dao.DistributorDao
import com.example.mvvmkotlinapp.repository.room.dao.MasterProductOrderDao
import com.example.mvvmkotlinapp.repository.room.dao.OutletDao
import io.reactivex.Single
import org.jetbrains.anko.doAsync
import java.util.concurrent.Callable

class OrderDeliveryRpository  (application: Application){

    private var database: AppDatabase? =null
    private var masterProductOrderDao: MasterProductOrderDao? = null
    private var outletDao: OutletDao? = null
    private var distributorDao: DistributorDao? = null


    init {
        database = AppDatabase.getDatabase(application)
        masterProductOrderDao = database?.masterProductOrderDao()
        outletDao = database?.outletDao()
        distributorDao = database?.distributorDao()

    }

    fun getOrderDeliveryList() = masterProductOrderDao?.getAllMasterProductOrders()

    fun getOutletNameFromID(outeletId:String) = outletDao?.getOutletNameFromID(outeletId)!!

    fun getDistributorNameFromID(distId:String) = distributorDao?.getOutletNameFromID(distId)!!


}