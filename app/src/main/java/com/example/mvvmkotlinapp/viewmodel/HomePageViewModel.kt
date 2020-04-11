package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mvvmkotlinapp.repository.HomePageRepository
import com.example.mvvmkotlinapp.repository.room.Features

class HomePageViewModel(application: Application) : AndroidViewModel(application) {


    private var repository:HomePageRepository = HomePageRepository(application)

    fun getFeatureList() = repository.getFeatureList()


}