package com.example.mvvmkotlinapp.repository

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import com.example.mvvmkotlinapp.repository.room.*
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import io.reactivex.Single
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.util.concurrent.Callable

class StartDutyRepository (application: Application){

    private var startDutyStatusDao:StartDutyStatusDao? = null
    private var database: AppDatabase? =null

    init {
        database = AppDatabase.getDatabase(application)
        startDutyStatusDao = database!!.startDutyStatusDao()
    }

    fun insertStartDutyStatus(student: StartDutyStatus): Single<Long>? {
        return Single.fromCallable(
            Callable<Long> { startDutyStatusDao!!.insertStatus(student) }
        )
    }


    fun insertStatus(context: Context?, startDutyStatus: StartDutyStatus?) {
        doAsync {
            database!!.startDutyStatusDao().insertStatus(startDutyStatus!!)
        }
    }

    fun getStartDutyStatus(): LiveData<StartDutyStatus>? = startDutyStatusDao?.getStatus()
}