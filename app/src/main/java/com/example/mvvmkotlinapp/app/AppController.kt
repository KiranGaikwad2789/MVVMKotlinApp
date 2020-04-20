package com.example.mvvmkotlinapp.app

import android.app.Application
import android.content.Context
import android.content.Intent

open class AppController : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: AppController? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = AppController.applicationContext()
    }
}