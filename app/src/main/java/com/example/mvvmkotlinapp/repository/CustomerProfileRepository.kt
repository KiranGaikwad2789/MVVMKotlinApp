package com.example.mvvmkotlinapp.repository

import android.app.Application
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.dao.OutletDetailsDao
import com.example.mvvmkotlinapp.repository.room.tables.OutletDetails
import io.reactivex.Single
import java.util.concurrent.Callable

class CustomerProfileRepository(application: Application) {

    private var outletDetailsDao: OutletDetailsDao? = null
    var database: AppDatabase? =null

    init {
        database = AppDatabase.getDatabase(application)
        outletDetailsDao = database?.outletDetailsDao()
    }

    fun getOutletDetails(outletId: String) = outletDetailsDao?.getOutletDetails(outletId)

    fun insertOutletDetails(outletDetails: OutletDetails): Single<Long>? {
        return Single.fromCallable(
            Callable<Long> {
                outletDetailsDao!!.insertOutletDetails(outletDetails)
            }
        )
    }

    fun updateOutletDetails(outletDetails: OutletDetails): Single<Int>? {
        return Single.fromCallable(
            Callable<Int> {
                outletDetailsDao!!.updateOutletDetailsTable(outletDetails)
            }
        )
    }
}