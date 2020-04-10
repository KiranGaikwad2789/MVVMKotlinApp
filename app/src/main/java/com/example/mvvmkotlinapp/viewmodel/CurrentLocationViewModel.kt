package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmkotlinapp.model.LoginInfo
import com.example.mvvmkotlinapp.repository.CurrentLocationRepository
import com.example.mvvmkotlinapp.repository.LoginRepository
import com.example.mvvmkotlinapp.repository.room.CurrentLocation


class CurrentLocationViewModel: ViewModel() {

    companion object{
        var mInstance: CurrentLocationRepository? = null
        public fun getmInstance(): CurrentLocationRepository? {
            if (mInstance == null) {
                mInstance = CurrentLocationRepository()
            }
            return mInstance
        }
    }
    private var currentLocation: MutableLiveData<CurrentLocation>? = null

    private var currentLocationRepository: CurrentLocationRepository? = null

    fun MainViewModel(application: Application) {
        currentLocationRepository = CurrentLocationRepository()
    }

    /*fun getAllLocations(context: Context): List<CurrentLocation> {
        return currentLocationRepository!!.getAllLocation()
    }*/

    fun getLoginData(): LiveData<CurrentLocation?>? {
        return currentLocation
    }
}