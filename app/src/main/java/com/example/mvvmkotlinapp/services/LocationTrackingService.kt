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
import android.os.*
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.mvvmkotlinapp.common.DateTime
import com.example.mvvmkotlinapp.repository.CurrentLocationRepository
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.CurrentLocation
import com.google.android.gms.location.*
import org.jetbrains.anko.doAsync
import java.text.DecimalFormat
import java.util.*
import com.example.mvvmkotlinapp.R
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class LocationTrackingService : Service(),LocationListener {

    //https://medium.com/@developer.mehul/android-location-tracking-with-a-background-service-api-level-26-oreo-ca47da2b89f6

    companion object{
        private var timerCount = 0
    }

    private var mTimer: Timer? = null
    private var notify_interval: Long = 120000  //mTimer
    private var context: Context? = null
    private val mHandler = Handler()

    var isGPSEnable = false
    var isNetworkEnable = false
    //var latitude = 0.0
    //var longitude = 0.0
    var locationManager: LocationManager? = null
    var location: Location? = null
    var intent: Intent? = null
    private var currentDate: DateTime? =null
    lateinit var database: AppDatabase

    private lateinit var fusedLocationClient: FusedLocationProviderClient



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        this.context = this;
        mTimer = Timer()
        currentDate= DateTime()
        database= AppDatabase.getDatabase(context as LocationTrackingService)

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

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun fn_getlocation() {

        locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isGPSEnable = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isNetworkEnable = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        if (!isGPSEnable && !isNetworkEnable) {

            Toast.makeText(context, "No Service Provider is available", Toast.LENGTH_SHORT).show();
            Log.e("Both OFF: ","isGPSEnable and isNetworkEnable are both OFF")

        } else {

            /*val locationRequest = LocationRequest().setInterval(30000).setFastestInterval(30000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        for (location in locationResult.locations) {

                            Log.e("current fused  latitude", location!!.latitude.toString() + "")
                            Log.e("current fused longitude", location!!.longitude.toString() + "")

                            if (location != null) {

                                //getCurrentLocationAndStoreinDB()
                            }
                        }
                        // Few more things we can do here:
                        // For example: Update the location of user on server
                    }
                },
                Looper.myLooper()
            )*/


            if (isGPSEnable) {

                //Log.e("isGPSEnable: ","isGPSEnable")

                location = null

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }

                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
                if (locationManager != null) {
                    //Log.e("locationManager: ",""+locationManager)

                    location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    //Log.e("location: ",""+location)

                    if (location != null) {

                        getCurrentLocationAndStoreinDB(location!!,"G")
                    }
                }
            }else if (isNetworkEnable) {

               // Log.e("isNetworkEnable: ","isNetworkEnable")

                location = null

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0f, this)

                if (locationManager != null) {

                    location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                    if (location != null) {

                        getCurrentLocationAndStoreinDB(location!!,"N")
                    }
                }
            }else{

                val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)
                            for (location in locationResult.locations) {

                                Log.e("current fused  latitude", location!!.latitude.toString() + "")
                                Log.e("current fused longitude", location!!.longitude.toString() + "")

                                if (location != null) {

                                    //getCurrentLocationAndStoreinDB(location!!,"L")
                                }
                            }
                            // Few more things we can do here:
                            // For example: Update the location of user on server
                        }
                    },
                    Looper.myLooper()
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentLocationAndStoreinDB(location: Location, location_type:String) {

        var isMockLocation=location.isFromMockProvider

        if(isMockLocation==false){

            var locationRecord: CurrentLocation? =null
            var locationTotalDistance=0.0
            var currentDistance=0.0

            doAsync {

                locationRecord=database.locationDao()!!.getAllLocationList()
                Log.e("locationRecord: ",""+locationRecord)

                val currentDateTime = LocalDateTime.parse(currentDate!!.getDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                Log.e("currentDateTime: ",""+currentDateTime)

                val previousDateTime = LocalDateTime.parse(locationRecord!!.date_time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                Log.e("previousDateTime: ",""+previousDateTime)

                var duration=Duration.between(previousDateTime,currentDateTime)
                Log.e("duration: ",""+duration.toMinutes())






                if (TextUtils.isEmpty(locationRecord.toString())){

                    Log.e("If: ","If: "+locationRecord)


                    //need to insert
                }else{

                    //get current distance between last loc and current loc
                    currentDistance=haversine(locationRecord?.lattitude?.toDouble(), locationRecord?.longitude?.toDouble(),location.latitude,location.longitude)
                    //Log.e("currentDistance ",""+ currentDistance)
                    //Log.e("lastDistance ",""+ locationRecord?.distance!!.toDouble())

                    //locationTotalDistance= locationRecord?.distance!!.toDouble() + currentDistance.toDouble()
                    //Log.e("locationTotalDistance ",""+ DecimalFormat("##.##").format(locationTotalDistance))

                    if (currentDistance<2){
                        locationTotalDistance= locationRecord?.distance!!.toDouble() + currentDistance.toDouble()
                        //Log.e("locationTotalDistance ",""+ DecimalFormat("##.##").format(locationTotalDistance))

                    }else{
                        Log.e("currentDistance ",""+ currentDistance)
                    }

                    var currentLocation=CurrentLocation(0,location.latitude,location.longitude,DecimalFormat("##.####").format(locationTotalDistance).toDouble(),
                        currentDate!!.getDateTime(),location_type)
                    CurrentLocationRepository.getmInstance()!!.insertLocation(context,currentLocation)

                    //Log.e("Before timerCount:",""+timerCount)
                    timerCount=timerCount+1
                    //Log.e("After timerCount:",""+timerCount)
                }

            }

            if (timerCount==0){

                var currentLocation=CurrentLocation(0,location.latitude,location.longitude,DecimalFormat("##.####").format(locationTotalDistance).toDouble(),
                    currentDate!!.getDateTime(),location_type)
                CurrentLocationRepository.getmInstance()!!.insertLocation(context,currentLocation)

                //Log.e("Before timerCount:",""+timerCount)
                timerCount=timerCount+1
                //Log.e("After timerCount:",""+timerCount)
            }

        }else{
            Log.e("Mock location","Mock loc")
        }



    }

    override fun onLocationChanged(location: Location?) {

        //Log.e("TAG", "onLocationChanged: " + location);
        location?.set(location);
        //longitude= location!!.getLongitude();
        //latitude=location.getLatitude();
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

    fun haversine(lat1: Double?, long1: Double?, lat2: Double?, long2: Double?): Double {

        val dtor = 0.017453293 // In kilometers
        val r = 6371.0
        val rlat1: Double
        val rlong1: Double
        val rlat2: Double
        val rlong2: Double
        rlat1 = lat1!! * dtor
        rlong1 = long1!! * dtor
        rlat2 = lat2!! * dtor
        rlong2 = long2!! * dtor
        val dlon = rlong1 - rlong2
        val dlat = rlat1 - rlat2
        val a = Math.pow(Math.sin(dlat / 2), 2.0) + Math.cos(rlat1) * Math.cos(rlat2) * Math.pow(Math.sin(dlon / 2), 2.0)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return r * c
    }

}
//https://android--code.blogspot.com/2018/03/android-kotlin-service-example.html