package com.example.mvvmkotlinapp.repository

import android.app.Application
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.dao.OutletDetailsDao

class CustomerProfileRepository(application: Application) {

    private var outletDetailsDao: OutletDetailsDao? = null
    var database: AppDatabase? =null

    init {
        database = AppDatabase.getDatabase(application)
        outletDetailsDao = database?.outletDetailsDao()
    }

    fun getOutletDetails(outletId: String) = outletDetailsDao?.getOutletDetails(outletId)


}