package com.example.mvvmkotlinapp.services

import android.app.Service
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.example.mvvmkotlinapp.common.LocationUtils
import com.example.mvvmkotlinapp.repository.CurrentLocationRepository
import com.example.mvvmkotlinapp.repository.room.CurrentLocation
import java.util.*


class LocationTrackingService : Service() {

    private var mTimer: Timer? = null
    var notify_interval: Long = 30000
    private var context: Context? = null

    var str_receiver = "servicetutorial.service.receiver"
    var intent: Intent? = null
    private var locationUtils: LocationUtils? =null

    private val mHandler = Handler()
    private var location: Location? =null


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        this.context = this;
        locationUtils= LocationUtils()
        mTimer = Timer()
        mTimer!!.schedule(monitor, 5, notify_interval)
        intent = Intent(str_receiver)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY;
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy <<");
        if (mTimer != null) {
            mTimer!!.cancel();
        }
    }

    val monitor = object : TimerTask() {
        override fun run() {
            mHandler.post {

                Log.e("location ","loc")
                val loc = CurrentLocation(0, 21.22, 75.33)
                CurrentLocationRepository.getmInstance()!!.insertLocation(context,loc)
            }
        }
    }
}