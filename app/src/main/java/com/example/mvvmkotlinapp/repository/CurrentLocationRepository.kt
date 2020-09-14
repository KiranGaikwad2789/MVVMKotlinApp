package com.example.mvvmkotlinapp.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.interfaces.APIInterface
import com.example.mvvmkotlinapp.repository.retrofit.RetrofitInstance
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.CurrentLocation
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

    fun getAllLocation(context: Context?) {
        myDataBase= AppDatabase.getDatabase(context!!)
        doAsync {
            myDataBase!!.locationDao().getAllLocation()
        }
    }

}