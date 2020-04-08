package com.example.mvvmkotlinapp.repository

import android.content.Context
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.CurrentLocation
import org.jetbrains.anko.doAsync

class CurrentLocationRepository {

    private var myDataBase: AppDatabase? = null

    companion object{
        var mInstance: CurrentLocationRepository? = null
        public fun getmInstance(): CurrentLocationRepository? {
            if (mInstance == null) {
                mInstance = CurrentLocationRepository()
            }
            return mInstance
        }
    }

    fun insertLocation(context: Context?, location: CurrentLocation?) {
        myDataBase= AppDatabase.getDatabase(context!!)
        doAsync {
            myDataBase!!.locationDao().insertAll(location!!)
        }
    }

}