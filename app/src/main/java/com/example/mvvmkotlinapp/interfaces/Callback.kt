package com.example.mvvmkotlinapp.interfaces

import android.location.Location

interface Callback {

    fun updateUi(pLocation: Location)
}