package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.interfaces.BindingAdapters
import com.example.mvvmkotlinapp.model.RegisterUserModel
import com.example.mvvmkotlinapp.repository.HomePageRepository
import com.example.mvvmkotlinapp.repository.room.City
import com.example.mvvmkotlinapp.repository.room.Features
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.fragmets.CityListFragment
import com.example.mvvmkotlinapp.view.fragmets.HomePageFragment
import com.example.mvvmkotlinapp.view.fragmets.ReviewOrderFragment

class HomePageViewModel(application: Application) : AndroidViewModel(application),BindingAdapters<Features> {


    private var activity1: HomePageFragment? =null

    fun anotherClass(fragment: HomePageFragment) { // Save instance of main class for future use
        activity1 = fragment
    }


    private var repository:HomePageRepository = HomePageRepository(application)

    fun getFeatureList() = repository.getFeatureList()

    fun insertFeatureList(arrayList: ArrayList<Features>){
        repository.insertFeatures(arrayList)
    }

    fun deleteTable()=repository.deleteFeatureTable()


    //city
    fun insertCityList(arrayList: ArrayList<City>){
        repository.insertCity(arrayList)
    }

    fun getCityList() = repository.getCityList()

    fun deleteCityTable()=repository.deleteCityble()

    fun onCityClick(){
        Log.e("Viewmodel Enter","enter")
        activity1!!.loadFragment(CityListFragment())
    }

    fun onFeatureClick(){

    }


    override fun setData(models: Features) {
        Log.e("Feature :",""+models.featureName)
    }


}