package com.example.mvvmkotlinapp.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.util.Log
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.mvvmkotlinapp.common.DateTime
import com.example.mvvmkotlinapp.receiver.AlarmReceive
import com.example.mvvmkotlinapp.repository.HomePageRepository
import com.example.mvvmkotlinapp.repository.StartDutyRepository
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.StartDutyStatus
import com.example.mvvmkotlinapp.services.LocationTrackingService
import com.example.mvvmkotlinapp.view.fragmets.CityListFragment
import com.example.mvvmkotlinapp.view.fragmets.HomePageFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class HomePageViewModel(application: Application) : AndroidViewModel(application){

    private val context = application.applicationContext

    private var activity1: HomePageFragment? =null

    private var alarmMgr: AlarmManager? = null
    private var pendingIntent: PendingIntent? =null
    private var txtDutyTime: TextView? =null
    private var txtCityLocation: TextView? =null
    lateinit var database: AppDatabase




    fun anotherClass(fragment: HomePageFragment) { // Save instance of main class for future use
        activity1 = fragment
    }

    fun init(){
        database= AppDatabase.getDatabase(context)
        alarmMgr = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceive::class.java)
        pendingIntent = PendingIntent.getBroadcast(context, 1, alarmIntent, 0)
        LocalBroadcastManager.getInstance(context!!).registerReceiver(myBroadcastReceiver, IntentFilter("thisIsForMyFragment"));
    }

    private val myBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent != null) {
                Log.e("City name ",""+ intent.getExtras()!!.getString("city"))
            }
            if (intent != null) {
                txtCityLocation!!.text=intent.getStringExtra("city")
            }
        }
    }

    fun onDestroyView() {
        context?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(myBroadcastReceiver) };
    }

    private var repository:HomePageRepository = HomePageRepository(application)
    private var startDutyRepository:StartDutyRepository = StartDutyRepository(application)
    private val disposable = CompositeDisposable()
    private var currentDate: DateTime? = DateTime()

    //update interval for widget
    val UPDATE_INTERVAL = 1000L
    //Handler to repeat update
    private val updateWidgetHandler = Handler()


    fun getFeatureList() = repository.getFeatureList()

    fun onCityClick(textView: TextView){
        txtCityLocation=textView
        activity1!!.loadFragment(CityListFragment(),"dialog")


    }

    fun getStartDutyStatus(): LiveData<StartDutyStatus>? = startDutyRepository.getStartDutyStatus()


    fun insertStartDutyStatus(status: StartDutyStatus) {
        disposable.add(startDutyRepository.insertStartDutyStatus(status)
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableSingleObserver<Long>() {
                    override fun onSuccess(t: Long) {
                        //Log.e("ViewModel Insert", t.toString())
                        //status.postValue("1")
                    }
                    override fun onError(e: Throwable) {
                       // Log.e("ViewModel error", e.message)
                        //status.postValue(e.message)
                    }

                })!!
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onCheckedChanged(checked: Boolean, switch: Switch,textView:TextView) { // implementation
        txtDutyTime=textView
        Log.e("switch ischeck: ", "" + checked)

        if (switch.isChecked){

            updateWidgetHandler.postDelayed(updateWidgetRunnable, UPDATE_INTERVAL)

            var startDutyStatus=StartDutyStatus(1,"ON", currentDate!!.getDateTime())
            insertStartDutyStatus(startDutyStatus)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmMgr!!.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmMgr!!.setExact(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
            } else {
                alarmMgr!!.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
            }

        }else{

            updateWidgetHandler.removeCallbacks(updateWidgetRunnable);

            var startDutyStatus=StartDutyStatus(1,"OFF", currentDate!!.getDateTime())
            insertStartDutyStatus(startDutyStatus)

            Log.e("Alaram stop","stop")
            alarmMgr?.cancel(pendingIntent)

            val intent = Intent(context, LocationTrackingService::class.java)
            context?.stopService(intent)

            doAsync {
                var locationRecord=database.locationDao()!!.getAllLocationList()
                Log.e("total distance: ",""+locationRecord.distance)
                uiThread {
                    showTotalDistanceDialog(locationRecord.distance)
                }
            }



        }

       /* var startDutyStatus: LiveData<StartDutyStatus>? = startDutyRepository.getStartDutyStatus()
        if (startDutyStatus != null) {
            Log.e("switch status: ", "" + startDutyStatus)
        }
        if (startDutyStatus != null) {
            if (!TextUtils.isEmpty(startDutyStatus.toString()) || startDutyStatus != null) {
                if (startDutyStatus.value?.status.equals("ON")) {
                    switch.isChecked = true
                } else if (startDutyStatus.value?.status.equals("OFF")) {
                    switch.isChecked = false
                }
            }
        }*/
    }

    private fun showTotalDistanceDialog(distance: Double) {
        activity1!!.showDialog(distance)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private var updateWidgetRunnable: Runnable = Runnable {
        run {
            //Update UI
            txtDutyTime!!.text=currentDate!!.getDateFormater()
            updateWidgetHandler.postDelayed(updateWidgetRunnable, UPDATE_INTERVAL)
        }
    }

    fun deleteLocationTable()=repository.deleteLocationTable()

    fun getAllLocation() = repository.getAllLocation()

}