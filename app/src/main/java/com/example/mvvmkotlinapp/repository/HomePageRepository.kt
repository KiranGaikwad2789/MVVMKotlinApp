package com.example.mvvmkotlinapp.repository

import android.app.Application
import android.content.Context
import com.example.mvvmkotlinapp.repository.room.*
import org.jetbrains.anko.doAsync

class HomePageRepository (application: Application){

    private var featureDao: FeatureDao? = null
    private var cityDao: CityDao? = null

    var database: AppDatabase? =null

    init {
        database = AppDatabase.getDatabase(application)
        featureDao = database?.featureDao()
        cityDao = database?.cityDao()
    }

    //Feature Module
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

    fun deleteFeatureTable(){
        doAsync {
            featureDao!!.deleteTableAllEntry()
        }
    }


//City module
    fun insertCity(arrayListCity: ArrayList<City>) {
        doAsync {
            if (arrayListCity != null) {
                for (city in arrayListCity!!) {
                    cityDao!!.insertAll(city)
                }
            }
        }
    }

    fun getCityList() = cityDao?.getCityList()

    fun deleteCityble(){
        doAsync {
            cityDao!!.deleteTableAllEntry()
        }
    }


}