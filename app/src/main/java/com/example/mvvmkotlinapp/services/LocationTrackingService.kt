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


class LocationTrackingService : Service(),LocationListener {

    //https://medium.com/@developer.mehul/android-location-tracking-with-a-background-service-api-level-26-oreo-ca47da2b89f6

    companion object{
        private var timerCount = 0
    }

    private var mTimer: Timer? = null
    private var notify_interval: Long = 60000  //mTimer
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

                Log.e("isGPSEnable: ","isGPSEnable")

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

                        getCurrentLocationAndStoreinDB(location!!)
                    }
                }
            }else if (isNetworkEnable) {

                Log.e("isNetworkEnable: ","isNetworkEnable")

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

                        getCurrentLocationAndStoreinDB(location!!)
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

                                    //getCurrentLocationAndStoreinDB()
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

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun getCurrentLocationAndStoreinDB(location: Location) {

        var isMockLocation=location.isFromMockProvider

        if(isMockLocation==false){

            /*if (timerCount==0){
                location.latitude=21.282379  //shria  21.282379,74.1544551
                location.longitude=74.1544551
            }else if (timerCount==1){
                location.latitude=21.3692741  //nandurbar  21.3692741,74.2123104
                location.longitude=74.2123104
            }else if (timerCount==2){
                location.latitude=21.1130  //nijampur
                location.longitude=74.3309
            }else if (timerCount==3){
                location.latitude=20.9943975  //sakri  20.9943975,74.308734
                location.longitude=74.308734
            }else if (timerCount==4){
                location.latitude=20.9042  //dhule
                location.longitude=74.7749
            }else if (timerCount==5){
                location.latitude=20.9497  //pimplner
                location.longitude=74.1219
            }else if (timerCount==6){
                location.latitude=20.9943975  //sakri  20.9943975,74.308734
                location.longitude=74.308734
            }else if (timerCount==7){
                location.latitude=21.282379  //shria  21.282379,74.1544551
                location.longitude=74.1544551
            }*/


            /*if (timerCount==0){
                location.latitude=18.5679  //VimanNagar  21.3692741,74.2123104
                location.longitude=73.9143
            }else if (timerCount==1){
                location.latitude=18.5362  //Koregaon  20.9943975,74.308734
                location.longitude=73.8940
            }else if (timerCount==2){
                location.latitude=18.5462  //KalyaniNagar
                location.longitude=73.9040
            }else if (timerCount==3){
                location.latitude=18.5538  //Kharadi  20.9943975,74.308734
                location.longitude=73.9477
            }else if (timerCount==4){
                location.latitude=18.5089  //Hadapsar
                location.longitude=73.9259
            }else if (timerCount==5){
                location.latitude=18.5740162  //Wagholi  18.5740162,73.9618582
                location.longitude=73.9618582
            }else if (timerCount==6){
                location.latitude=18.555382  //Wadia  18.555382,73.8628655
                location.longitude=73.8628655
            }else if (timerCount==7){
                location.latitude=18.5679  //VimanNagar  21.3692741,74.2123104
                location.longitude=73.9143
            }*/


            var locationRecord: CurrentLocation? =null
            var locationTotalDistance=0.0

            doAsync {

                locationRecord=database.locationDao()!!.getAllLocationList()
                Log.e("locationRecord: ",""+locationRecord)

                var currentDistance=0.0

                if (TextUtils.isEmpty(locationRecord.toString())){

                    Log.e("If: ","If: "+locationRecord)


                    //need to insert
                }else{

                    currentDistance=haversine(locationRecord?.lattitude?.toDouble(), locationRecord?.longitude?.toDouble(),location.latitude,location.longitude)
                    Log.e("currentDistance ",""+ currentDistance)
                    Log.e("lastDistance ",""+ locationRecord?.distance!!.toDouble())

                    locationTotalDistance= locationRecord?.distance!!.toDouble() + currentDistance.toDouble()
                    Log.e("locationTotalDistance ",""+ DecimalFormat("##.##").format(locationTotalDistance))

                    /*if (currentDistance<2){
                        locationTotalDistance= locationRecord?.distance!!.toDouble() + currentDistance.toDouble()
                        Log.e("locationTotalDistance ",""+ DecimalFormat("##.##").format(locationTotalDistance))

                    }else{
                    }*/

                    var currentLocation=CurrentLocation(0,location.latitude,location.longitude,DecimalFormat("##.####").format(locationTotalDistance).toDouble())
                    CurrentLocationRepository.getmInstance()!!.insertLocation(context,currentLocation)

                    Log.e("Before timerCount:",""+timerCount)
                    timerCount=timerCount+1
                    Log.e("After timerCount:",""+timerCount)
                }

            }

            if (timerCount==0){

                var currentLocation=CurrentLocation(0,location.latitude,location.longitude,DecimalFormat("##.####").format(locationTotalDistance).toDouble())
                CurrentLocationRepository.getmInstance()!!.insertLocation(context,currentLocation)

                Log.e("Before timerCount:",""+timerCount)
                timerCount=timerCount+1
                Log.e("After timerCount:",""+timerCount)
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
            //.setSmallIcon(R.drawable.ic_home_black_24dp)
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


    fun isMockLocationOn(
        location: Location,
        context: Context
    ): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            location.isFromMockProvider
        } else {
            var mockLocation = "0"
            try {
                mockLocation = Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ALLOW_MOCK_LOCATION
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            mockLocation != "0"
        }
    }

}
//https://android--code.blogspot.com/2018/03/android-kotlin-service-example.html