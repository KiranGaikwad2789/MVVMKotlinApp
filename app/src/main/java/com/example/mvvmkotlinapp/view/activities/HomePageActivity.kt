package com.example.mvvmkotlinapp.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.repository.HomePageRepository
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.Features
import com.example.mvvmkotlinapp.view.fragmets.AlarmSettingFragment
import com.example.mvvmkotlinapp.view.fragmets.HomePageFragment
import com.example.mvvmkotlinapp.view.fragmets.MyProfileFragment
import com.example.mvvmkotlinapp.view.fragmets.ReviewOrderFragment
import com.google.android.material.navigation.NavigationView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread


class HomePageActivity : AppCompatActivity() ,NavigationView.OnNavigationItemSelectedListener{

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    private lateinit var mDb: AppDatabase

    var arrayListFeaturesInfo: List<Features>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        mDb = AppDatabase.getDatabase(applicationContext)

        doAsync {
            //arrayListFeaturesInfo=HomePageRepository.getmInstance()!!.getStatus(this@HomePageActivity)
            if (arrayListFeaturesInfo!!.size==0){
                insertFeatures()
            }
            uiThread {
                toast("${arrayListFeaturesInfo!!.size} features found.")
            }
        }


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

    private fun insertFeatures() {

        val feature1 = Features(1, "Capture Outlet")
        val feature2 = Features(2, "New Lead")
        val feature3 = Features(3, "Customer Profile")
        val feature4 = Features(4, "New order")
        val feature5 = Features(5, "Order Delivery")
        val feature6 = Features(6, "My Dashboard")
        val feature7 = Features(7, "Weekly Stock")
        val feature8 = Features(8, "Product menu")
        val feature9 = Features(9, "My DSR")
        val feature10 = Features(10, "My TimeSheet")
        val feature11 = Features(11, "My Claims")
        val feature12 = Features(12, "Manage Leave")
        val feature13 = Features(13, "Customer Support")

        var arrayListFeaturesInfo= ArrayList<Features>()

        if (arrayListFeaturesInfo != null) {
            arrayListFeaturesInfo!!.add(feature1)
            arrayListFeaturesInfo!!.add(feature2)
            arrayListFeaturesInfo!!.add(feature3)
            arrayListFeaturesInfo!!.add(feature4)
            arrayListFeaturesInfo!!.add(feature5)
            arrayListFeaturesInfo!!.add(feature6)
            arrayListFeaturesInfo!!.add(feature7)
            arrayListFeaturesInfo!!.add(feature8)
            arrayListFeaturesInfo!!.add(feature9)
            arrayListFeaturesInfo!!.add(feature10)
            arrayListFeaturesInfo!!.add(feature11)
            arrayListFeaturesInfo!!.add(feature12)
            arrayListFeaturesInfo!!.add(feature13)
        }
        Log.e("features size : ",""+ arrayListFeaturesInfo!!.size)
       // HomePageRepository.getmInstance()!!.deleteEntry(this)

        //HomePageRepository.getmInstance()!!.insertFeatures(this, arrayListFeaturesInfo!!)

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
