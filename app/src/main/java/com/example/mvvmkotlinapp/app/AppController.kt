package com.example.mvvmkotlinapp.app

import android.app.Application
import android.content.Context
import android.content.Intent
import com.example.mvvmkotlinapp.utils.ModelPreferencesManager

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
        ModelPreferencesManager.with(this)
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = AppController.applicationContext()
    }
}