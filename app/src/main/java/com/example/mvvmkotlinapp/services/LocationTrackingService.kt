package com.example.mvvmkotlinapp.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.DateTime
import com.example.mvvmkotlinapp.repository.CurrentLocationRepository
import com.example.mvvmkotlinapp.repository.room.CurrentLocation
import java.util.*


class LocationTrackingService : Service(),LocationListener {

    companion object{
        private val NOTIF_ID = 1
    }

    private var mTimer: Timer? = null
    private var notify_interval: Long = 10000
    private var context: Context? = null
    private val mHandler = Handler()

    var isGPSEnable = false
    var isNetworkEnable = false
    var latitude = 0.0
    var longitude:kotlin.Double = 0.0
    var locationManager: LocationManager? = null
    var location: Location? = null
    var intent: Intent? = null
    private var currentDate: DateTime? =null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        this.context = this;
        mTimer = Timer()
        currentDate= DateTime()
        mTimer!!.schedule(monitor, 0, notify_interval)
        startForeGroundService(currentDate!!.getDateFormater())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("Destroy service", "onDestroy <<");
        if (mTimer != null) {
            mTimer!!.cancel();
            mHandler.removeCallbacks(monitor)
        }

    }

    val monitor = object : TimerTask() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            mHandler.post {
                fn_getlocation()
            }
        }
    }

    private fun fn_getlocation() {
        locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isGPSEnable = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isNetworkEnable = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGPSEnable && !isNetworkEnable) {
        } else {
            if (isNetworkEnable) {
                location = null
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                locationManager!!.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    0f,
                    this
                )
                if (locationManager != null) {
                    location =
                        locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if (location != null) {
                        Log.e("latitude", location!!.latitude.toString() + "")
                        Log.e("longitude", location!!.longitude.toString() + "")
                        latitude = location!!.latitude
                        longitude = location!!.longitude

                        Log.e("DB loc ", "=="+ latitude);
                        var currentLocation=CurrentLocation(0,latitude,longitude)
                        CurrentLocationRepository.getmInstance()!!.insertLocation(context,currentLocation)
                    }
                }
            }
            if (isGPSEnable) {
                location = null
                locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    0f,
                    this
                )
                if (locationManager != null) {
                    location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location != null) {
                        Log.e("latitude", location!!.latitude.toString() + "")
                        Log.e("longitude", location!!.longitude.toString() + "")
                        latitude = location!!.latitude
                        longitude = location!!.longitude

                        Log.e("DB loc ", "=="+ latitude);
                        var currentLocation=CurrentLocation(0,latitude,longitude)
                        CurrentLocationRepository.getmInstance()!!.insertLocation(context,currentLocation)                    }
                }
            }
        }
    }

    override fun onLocationChanged(location: Location?) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForeGroundService(dateFormater: String): Notification {

        //val input = intent?.getStringExtra("inputExtra")
        val channel = NotificationChannel(1.toString(), "My Channel", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        //val notificationIntent = Intent(this, HomePageActivity::class.java)
        //val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification = NotificationCompat.Builder(this, 1.toString())
            .setContentTitle("Skygge start duty on..")
            .setContentText(dateFormater)
            .setSmallIcon(R.drawable.ic_home_black_24dp)
           // .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        val builder: Notification.Builder = Notification.Builder(applicationContext, "channel_01").setAutoCancel(false)
        return builder.build()
    }

}
//https://android--code.blogspot.com/2018/03/android-kotlin-service-example.html