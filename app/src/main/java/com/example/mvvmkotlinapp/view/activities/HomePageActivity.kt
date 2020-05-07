package com.example.mvvmkotlinapp.view.activities

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.ActivityHomePageBinding
import com.example.mvvmkotlinapp.interfaces.DrawerLocker
import com.example.mvvmkotlinapp.utils.AlertDialog
import com.example.mvvmkotlinapp.view.fragmets.AlarmSettingFragment
import com.example.mvvmkotlinapp.view.fragmets.HomePageFragment
import com.example.mvvmkotlinapp.view.fragmets.MyProfileFragment
import com.example.mvvmkotlinapp.viewmodel.HomeMainViewModel
import com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_LABELED
import kotlinx.android.synthetic.main.app_bar_home_page.*
import kotlinx.android.synthetic.main.nav_header_home_page.view.*


class HomePageActivity : AppCompatActivity(), DrawerLocker {

    private var binding: ActivityHomePageBinding? = null
    private var homeMainViewModel: HomeMainViewModel? = null
    private var userSession: UserSession? =null
    var toggle: ActionBarDrawerToggle? =null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeBindingView()
        initializeObjects()

        commonMethodForFragment(HomePageFragment(), true)
    }

    private fun initializeBindingView() {
        homeMainViewModel = ViewModelProviders.of(this).get(HomeMainViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page)
        binding?.lifecycleOwner = this
        binding?.homeModel=homeMainViewModel


    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initializeObjects() {

        setSupportActionBar(toolbar)
        homeMainViewModel!!.anotherClass(this)
        userSession=UserSession(this)

        toggle = ActionBarDrawerToggle(this, binding?.drawerLayout, toolbar, 0, 0)
        binding?.drawerLayout!!.addDrawerListener(toggle!!)
        toggle!!.syncState()

        binding!!.navigation!!.isItemHorizontalTranslationEnabled = true
        binding!!.navigation.labelVisibilityMode= LABEL_VISIBILITY_LABELED

        val headerView = binding!!.navView.getHeaderView(0)
        headerView.txtUserName.text = userSession!!.getUsername()
        headerView.txtAppVersion.text = userSession!!.getEmail()

        headerView.llHomePage.setOnClickListener { commonMethodForFragment(HomePageFragment(), false) }
        headerView.llMyProfile.setOnClickListener { commonMethodForFragment(MyProfileFragment(), true) }
        headerView.llAlarmSetting.setOnClickListener { commonMethodForFragment(AlarmSettingFragment(), true) }
        headerView.llBackup.setOnClickListener { AlertDialog.basicAlert(this, getString(R.string.backup_title), getString(R.string.backup_message),"1") }
        headerView.llFetchSettingData.setOnClickListener { AlertDialog.basicAlert(this, getString(R.string.fetchdata_title),getString(R.string.fetchdata_message),"1")  }


    }

    override fun setDrawerEnabled(enabled: Boolean) {
        val lockMode = if (enabled) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        binding?.drawerLayout?.setDrawerLockMode(lockMode)
        toggle!!.isDrawerIndicatorEnabled=enabled
    }

    fun commonMethodForFragment(fragment: Fragment, addToBackStack: Boolean) {

        if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding!!.drawerLayout.closeDrawer(GravityCompat.START);
        }

        val uiHandler = Handler()
        uiHandler.post {

            val myFragmentManager: FragmentManager = supportFragmentManager
            val backStateName: String = fragment.javaClass.simpleName
            val fragmentPopped: Boolean = myFragmentManager.popBackStackImmediate(backStateName, 0)

            val myFragmentTransaction: FragmentTransaction = myFragmentManager.beginTransaction()
            myFragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            if (!fragmentPopped && myFragmentManager.findFragmentByTag(backStateName) == null) {

                if (addToBackStack) { //fragment not in back stack, create it.

                    myFragmentTransaction.replace(R.id.container, fragment, backStateName)
                    myFragmentTransaction.addToBackStack(backStateName)
                    myFragmentTransaction.commitAllowingStateLoss()

                } else {

                    myFragmentTransaction.replace(R.id.container, fragment, backStateName)
                    myFragmentTransaction.commitAllowingStateLoss()

                }

            } else {

                myFragmentTransaction.replace(R.id.container, fragment)
                myFragmentTransaction.addToBackStack(null)
                myFragmentTransaction.commitAllowingStateLoss()

            }
        }
    }


    override fun onBackPressed() {

        if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding!!.drawerLayout.closeDrawer(GravityCompat.START);
        }
        var isBack = true
        val f = supportFragmentManager.findFragmentById(R.id.container)

        if (f != null) {
            when (f.javaClass.simpleName) {

                "HomePageFragment" -> {
                    Log.e("HomePageFragment ","HomePageFragment")
                    isBack = false
                    finish()
                }
            }
        }
        visibleMenuItems(0)
        if (isBack) super.onBackPressed()
    }

    fun visibleMenuItems(featureId:Int){
        if (featureId==0){
            binding!!.navigation.isVisible = true
            binding!!.navigation.menu.findItem(R.id.nav_review_order).isVisible = true
            binding!!.navigation.menu.findItem(R.id.nav_uncapture_outlet).isVisible = true
            binding!!.navigation.menu.findItem(R.id.nav_uncapture_outlet).setTitle("Un Captured Outlet")
            binding!!.navigation.menu.findItem(R.id.nav_new_lead).isVisible = false
            binding!!.navigation.menu.findItem(R.id.nav_take_order).isVisible = false
            binding!!.navigation.menu.findItem(R.id.nav_offer_details).isVisible = false
        }else if (featureId==1 || featureId==2 ||featureId==4 ||featureId==11){
            binding!!.navigation.isVisible = true
            binding!!.navigation.menu.findItem(R.id.nav_review_order).isVisible = false
            binding!!.navigation.menu.findItem(R.id.nav_uncapture_outlet).isVisible = true
            binding!!.navigation.menu.findItem(R.id.nav_uncapture_outlet).setTitle("Outlet")
            binding!!.navigation.menu.findItem(R.id.nav_new_lead).isVisible = true
            binding!!.navigation.menu.findItem(R.id.nav_take_order).isVisible = true
            binding!!.navigation.menu.findItem(R.id.nav_offer_details).isVisible = true
        }else {
            binding!!.navigation.isVisible = false
        }

    }




}
