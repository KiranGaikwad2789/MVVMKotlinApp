package com.example.mvvmkotlinapp.view.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.SystemClock
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.LocationUtils
import com.example.mvvmkotlinapp.receiver.AlarmReceive
import com.example.mvvmkotlinapp.repository.room.CurrentLocation
import com.example.mvvmkotlinapp.view.fragmets.AlarmSettingFragment
import com.example.mvvmkotlinapp.view.fragmets.HomePageFragment
import com.example.mvvmkotlinapp.view.fragmets.MyProfileFragment
import com.example.mvvmkotlinapp.view.fragmets.ReviewOrderFragment
import com.google.android.material.navigation.NavigationView


class HomePageActivity : AppCompatActivity() ,NavigationView.OnNavigationItemSelectedListener{

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        loadFragment(HomePageFragment())
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_home_page -> {
                loadFragment(HomePageFragment())
            }
            R.id.nav_my_profile -> {
                loadFragment(MyProfileFragment())
            }
            R.id.nav_alarm_setting -> {
                loadFragment(AlarmSettingFragment())
            }
            R.id.nav_backup -> {
                loadFragment(HomePageFragment())
            }
            R.id.nav_fetch_data -> {
                loadFragment(HomePageFragment())
            }

            R.id.nav_review_order -> {
                loadFragment(ReviewOrderFragment())
            }
            R.id.nav_uncapture_outlet -> {
                loadFragment(HomePageFragment())
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }
}
