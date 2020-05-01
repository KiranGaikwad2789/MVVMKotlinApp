package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.os.Build
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.repository.HomePageRepository
import com.example.mvvmkotlinapp.utils.AlertDialog
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.fragmets.AlarmSettingFragment
import com.example.mvvmkotlinapp.view.fragmets.HomePageFragment
import com.example.mvvmkotlinapp.view.fragmets.MyProfileFragment
import com.example.mvvmkotlinapp.view.fragmets.ReviewOrderFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class HomeMainViewModel(application: Application) : AndroidViewModel(application), BottomNavigationView.OnNavigationItemSelectedListener,NavigationView.OnNavigationItemSelectedListener{

    private var repository: HomePageRepository = HomePageRepository(application)
    private var activity1: HomePageActivity? =null

    fun anotherClass(activity: HomePageActivity) { // Save instance of main class for future use
        activity1 = activity
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_home_page -> {
                (activity1 as HomePageActivity).commonMethodForFragment(HomePageFragment(), false)
                //activity1!!.loadFragment(HomePageFragment())
                return true
            }
            R.id.nav_my_profile -> {
                (activity1 as HomePageActivity).commonMethodForFragment(MyProfileFragment(), true)
                //activity1!!.loadFragment(MyProfileFragment())
                return true
            }
            R.id.nav_alarm_setting -> {
                (activity1 as HomePageActivity).commonMethodForFragment(AlarmSettingFragment(), true)
                //activity1!!.loadFragment(AlarmSettingFragment())
                return true
            }
            R.id.nav_backup -> {
                activity1?.let { AlertDialog.basicAlert(it, activity1!!.getString(R.string.backup_title),activity1!!.getString(R.string.backup_message),"1") }
                return true
            }
            R.id.nav_fetch_data -> {
                activity1?.let { AlertDialog.basicAlert(it, activity1!!.getString(R.string.fetchdata_title),activity1!!.getString(R.string.fetchdata_message),"1") }
                return true
            }
            R.id.nav_review_order -> {
                (activity1 as HomePageActivity).commonMethodForFragment(ReviewOrderFragment(), true)
                //activity1!!.loadFragment(ReviewOrderFragment())
                return true
            }
            R.id.nav_uncapture_outlet -> {
                (activity1 as HomePageActivity).commonMethodForFragment(ReviewOrderFragment(), true)
                //activity1!!.loadFragment(ReviewOrderFragment())
                return true
            }
            R.id.nav_new_lead -> {
                (activity1 as HomePageActivity).commonMethodForFragment(ReviewOrderFragment(), true)
                //activity1!!.loadFragment(ReviewOrderFragment())
                return true
            }
            R.id.nav_take_order -> {
                (activity1 as HomePageActivity).commonMethodForFragment(ReviewOrderFragment(), true)
                //activity1!!.loadFragment(ReviewOrderFragment())
                return true
            }
            R.id.nav_offer_details -> {
                activity1?.let { AlertDialog.basicAlert(it, activity1!!.getString(R.string.fetchlatestdata_title),activity1!!.getString(R.string.fetchlatestdata_message),"1") }
                return true
            }
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onFetchServerDataClicked(){
        activity1?.let { AlertDialog.basicAlert(it, activity1!!.getString(R.string.fetchlatestdata_title),activity1!!.getString(R.string.fetchlatestdata_message),"1") }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onSyncDataClicked(){
        activity1?.let { AlertDialog.basicAlert(it, activity1!!.getString(R.string.syncdata_title),activity1!!.getString(R.string.syncdata_message),"1") }
    }

    fun onNotificationClicked(){

    }

}