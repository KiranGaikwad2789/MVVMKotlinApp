package com.example.mvvmkotlinapp.repository

import android.app.Application
import android.content.Context
import com.example.mvvmkotlinapp.repository.room.*
import org.jetbrains.anko.doAsync

class HomePageRepository (application: Application){

    private var featureDao: FeatureDao? = null
    private var cityDao: CityDao? = null
    private var currentLocationDao: CurrentLocationDao? = null


    var database: AppDatabase? =null

    init {
        database = AppDatabase.getDatabase(application)
        featureDao = database?.featureDao()
        cityDao = database?.cityDao()
        currentLocationDao = database?.locationDao()
    }

    //Feature list
    fun getFeatureList() = featureDao?.getFeatureList()

    fun insertFeatures(arrayListFeaturesInfo: ArrayList<Features>) {
        doAsync {
            if (arrayListFeaturesInfo != null) {
                for (features in arrayListFeaturesInfo!!) {
                    featureDao!!.insertAll(features)
                }
            }
        }
    }

    fun getCityList() = cityDao?.getCityList()

    fun getAllLocation() = currentLocationDao?.getAllLocation()

    fun deleteLocationTable(){
        doAsync {
            currentLocationDao!!.deleteTableAllEntry()
        }
    }


}