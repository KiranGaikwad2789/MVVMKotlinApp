package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.mvvmkotlinapp.repository.HomePageRepository
import com.example.mvvmkotlinapp.repository.room.City

class CityListViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: HomePageRepository = HomePageRepository(application)


    fun getCityList() = repository.getCityList()

}