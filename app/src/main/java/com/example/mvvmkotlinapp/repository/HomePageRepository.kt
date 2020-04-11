package com.example.mvvmkotlinapp.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.FeatureDao
import com.example.mvvmkotlinapp.repository.room.Features
import com.example.mvvmkotlinapp.repository.room.StartDutyStatus
import org.jetbrains.anko.doAsync
import kotlin.coroutines.CoroutineContext

class HomePageRepository (application: Application){

    private var featureDao: FeatureDao? = null

    init {
        val db = AppDatabase.getDatabase(application)
        featureDao = db?.featureDao()
    }

    fun getFeatureList() = featureDao?.getFeatureList()


    fun insertFeatures(context: Context?, arrayListFeaturesInfo: ArrayList<Features>) {
        doAsync {
            if (arrayListFeaturesInfo != null) {
                for (features in arrayListFeaturesInfo!!) {
                    AppDatabase.getDatabase(context!!)!!.featureDao().insertAll(features)
                } }
        }
    }

   /* fun getStatus(context: Context?): List<Features> {
        return AppDatabase.getDatabase(context!!).featureDao().getFeatureList()
    }*/

    fun deleteEntry(context: Context?) {
        doAsync {
            AppDatabase.getDatabase(context!!).featureDao().deleteTableAllEntry()
        }
    }




}