package com.example.mvvmkotlinapp.repository

import android.app.Application
import com.example.mvvmkotlinapp.repository.room.*
import com.example.mvvmkotlinapp.repository.room.dao.DistributorDao
import com.example.mvvmkotlinapp.repository.room.dao.OutletDao
import com.example.mvvmkotlinapp.repository.room.dao.RouteDao
import io.reactivex.Single
import java.util.concurrent.Callable

class CaptureOutletRepository(application: Application) {

    private var routeDao: RouteDao? = null
    private var outletDao: OutletDao? = null
    private var distributorDao: DistributorDao? = null


    var database: AppDatabase? =null

    init {
        database = AppDatabase.getDatabase(application)
        routeDao = database?.routeDao()
        outletDao = database?.outletDao()
        distributorDao = database?.distributorDao()
    }

    //Route list
    fun getRouteList() = routeDao?.getAllRoutes()

    fun getOutletList() = outletDao?.getAllOutlets()

    fun getDistList() = distributorDao?.getAllDistributors()

    fun getAllDistributorsList():Single<List<Distributor>>?{
        return Single.fromCallable(
            Callable<List<Distributor>> { distributorDao?.getAllDistributorsList() }
        )
    }

    fun getAllRoutesList(): Single<List<Route>>? {
        return Single.fromCallable(
            Callable<List<Route>> { routeDao?.getAllRoutesList() }
        )
    }

    fun getOutletListFromID(spinnerValueID: String, radioGroupType: String):Single<List<Outlet>>?{
        return Single.fromCallable(
            Callable<List<Outlet>> {
                if (radioGroupType.equals("By Route"))
                    outletDao?.getOutletListFromRouteID(spinnerValueID)
                else
                    outletDao?.getOutletListFromDistID(spinnerValueID)
            }
        )
    }

}