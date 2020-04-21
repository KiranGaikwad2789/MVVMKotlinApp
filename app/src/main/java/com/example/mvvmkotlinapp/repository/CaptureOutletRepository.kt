package com.example.mvvmkotlinapp.repository

import android.app.Application
import android.util.Log
import com.example.mvvmkotlinapp.repository.room.*
import com.example.mvvmkotlinapp.repository.room.dao.DistributorDao
import com.example.mvvmkotlinapp.repository.room.dao.OutletDao
import com.example.mvvmkotlinapp.repository.room.dao.RouteDao
import io.reactivex.Single
import org.jetbrains.anko.doAsync
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

    //Outlet list
    /*fun getOutletList(route_id:String): List<Outlet>? {
        var arrayList: List<Outlet>? = null
        doAsync {
            arrayList=outletDao!!.getAllOutlets(route_id)
            Log.e("Oultet list1: ",""+ arrayList!!.size)
        }
        return arrayList
    }*/


   /* fun getOutletList(route_id:String): Single<List<Outlet>>? {
        return Single.fromCallable(
            Callable<List<Outlet>> { outletDao?.getAllOutlets(route_id) }
        )
    }*/


}