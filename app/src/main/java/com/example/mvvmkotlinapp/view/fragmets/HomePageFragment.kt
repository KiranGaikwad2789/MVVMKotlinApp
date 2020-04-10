package com.example.mvvmkotlinapp.view.fragmets

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.mvvmkotlinapp.BuildConfig
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.DateTime
import com.example.mvvmkotlinapp.databinding.FragmentHomePageBinding
import com.example.mvvmkotlinapp.receiver.AlarmReceive
import com.example.mvvmkotlinapp.repository.CurrentLocationRepository
import com.example.mvvmkotlinapp.repository.StartDutyRepository
import com.example.mvvmkotlinapp.repository.room.CurrentLocation
import com.example.mvvmkotlinapp.repository.room.StartDutyStatus
import com.example.mvvmkotlinapp.services.ForegroundService.Companion.stopService
import com.example.mvvmkotlinapp.services.LocationTrackingService
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.viewmodel.HomePageViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread


class HomePageFragment : Fragment() {

    lateinit var homePageViewModel:HomePageViewModel
    lateinit var homePageBinding : FragmentHomePageBinding

    private var alarmMgr: AlarmManager? = null
    private var pendingIntent: PendingIntent? =null
    private var currentDate: DateTime? =null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homePageBinding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_home_page, container, false)
        val view: View = homePageBinding.getRoot()

        initViews()
        getStartDutyStatus()

        homePageBinding.switchStartDuty.setOnCheckedChangeListener({ view , isChecked ->

            if (isChecked){
                var startDutyStatus=StartDutyStatus(1,"ON", currentDate!!.getDateTime())
                StartDutyRepository.getmInstance()!!.insertStatus(context,startDutyStatus)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmMgr!!.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmMgr!!.setExact(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
                } else {
                    alarmMgr!!.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
                }
            }else{

                var startDutyStatus=StartDutyStatus(1,"OFF", currentDate!!.getDateTime())
                StartDutyRepository.getmInstance()!!.insertStatus(context,startDutyStatus)
                    Log.e("Alaram stop","stop")
                    alarmMgr?.cancel(pendingIntent)

                val intent = Intent(context, LocationTrackingService::class.java)
                context?.stopService(intent)

                /*if (isServiceRunning(LocationTrackingService::class.java)) {
                    // Stop the service
                    val intent = Intent(context, LocationTrackingService::class.java)
                    context?.stopService(intent)
                } else {
                    Log.e("Service already stop","stop")
                }*/
            }
        })
        return view
    }

    private fun initViews() {
        currentDate= DateTime()
        alarmMgr = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(activity, AlarmReceive::class.java)
        pendingIntent = PendingIntent.getBroadcast(activity, 1, alarmIntent, 0)
    }

    private fun getStartDutyStatus() {

        doAsync {

            var startDutyStatus:StartDutyStatus?=StartDutyRepository.getmInstance()!!.getStatus(activity)
            Log.e("status", startDutyStatus?.status)

            uiThread {
                if (startDutyStatus != null) {
                    if (!startDutyStatus.equals(startDutyStatus) || !TextUtils.isEmpty(startDutyStatus.toString()) || startDutyStatus!=null){
                        if (startDutyStatus.status.equals("ON")){
                            homePageBinding.switchStartDuty.isChecked=true
                        }else if (startDutyStatus.status.equals("OFF")){
                            homePageBinding.switchStartDuty.isChecked=false
                        }
                    }
                }
            }
        }
    }
}
