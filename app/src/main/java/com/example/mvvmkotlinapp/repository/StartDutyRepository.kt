package com.example.mvvmkotlinapp.repository

import android.content.Context
import android.content.Intent
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.CurrentLocation
import com.example.mvvmkotlinapp.repository.room.StartDutyStatus
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class StartDutyRepository {

    private var myDataBase: AppDatabase? = null

    companion object{
        var mInstance: StartDutyRepository? = null
        public fun getmInstance(): StartDutyRepository? {
            if (mInstance == null) {
                mInstance = StartDutyRepository()
            }
            return mInstance
        }
    }

    fun insertStatus(context: Context?, startDutyStatus: StartDutyStatus?) {
        myDataBase= AppDatabase.getDatabase(context!!)
        doAsync {
            myDataBase!!.startDutyStatusDao().insertStatus(startDutyStatus!!)
        }
    }

    fun getStatus(context: Context?): StartDutyStatus {
        return AppDatabase.getDatabase(context!!).startDutyStatusDao().getStatus()
    }
}