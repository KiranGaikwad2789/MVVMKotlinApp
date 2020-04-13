package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mvvmkotlinapp.repository.HomePageRepository
import com.example.mvvmkotlinapp.repository.room.City
import com.example.mvvmkotlinapp.repository.room.Features

class HomePageViewModel(application: Application) : AndroidViewModel(application) {

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



}