package com.example.mvvmkotlinapp.app

import android.app.Application
import android.content.Intent

class AppController : Application() {

    private var mInstance: AppController? = null

    fun getmInstance(): AppController? {
        return mInstance
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }
}