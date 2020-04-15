package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.AndroidViewModel
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.repository.HomePageRepository
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


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {


            R.id.nav_home_page -> {
                activity1!!.loadFragment(HomePageFragment())
                return true
            }
            R.id.nav_my_profile -> {
                activity1!!.loadFragment(MyProfileFragment())
                return true
            }
            R.id.nav_alarm_setting -> {
                activity1!!.loadFragment(AlarmSettingFragment())
                return true
            }
            R.id.nav_backup -> {
                activity1!!.loadFragment(HomePageFragment())
                return true
            }
            R.id.nav_fetch_data -> {
                activity1!!.loadFragment(HomePageFragment())
                return true
            }
            R.id.nav_review_order -> {
                activity1!!.loadFragment(ReviewOrderFragment())
                return true
            }
            R.id.nav_uncapture_outlet -> {
                activity1!!.loadFragment(HomePageFragment())
                return true
            }
            R.id.nav_new_lead -> {
                activity1!!.loadFragment(HomePageFragment())
                return true
            }
            R.id.nav_take_order -> {
                activity1!!.loadFragment(HomePageFragment())
                return true
            }
            R.id.nav_offer_details -> {
                activity1!!.loadFragment(HomePageFragment())
                return true
            }
        }
        return false
    }

}