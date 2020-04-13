package com.example.mvvmkotlinapp.view.activities

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.ActivityHomePageBinding
import com.example.mvvmkotlinapp.view.fragmets.HomePageFragment
import com.example.mvvmkotlinapp.viewmodel.HomeMainViewModel
import kotlinx.android.synthetic.main.app_bar_home_page.*
import kotlinx.android.synthetic.main.nav_header_home_page.view.*


class HomePageActivity : AppCompatActivity() {//,NavigationView.OnNavigationItemSelectedListener{

    //https://stackoverflow.com/questions/46872747/bottomnavigationview-on-databinding-inside-viewmodel
    //https://medium.com/@oldergod/bottomnavigationview-callback-and-and-databinding-9775b0c31d38


    lateinit var binding: ActivityHomePageBinding
    private var homeMainViewModel: HomeMainViewModel? = null
    private var userSession: UserSession? =null
    var navigationPosition: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeMainViewModel = ViewModelProviders.of(this).get(HomeMainViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page)
        binding.lifecycleOwner = this
        binding.homeModel=homeMainViewModel
        setSupportActionBar(toolbar)
        homeMainViewModel!!.anotherClass(this)
        userSession=UserSession(this)

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, toolbar, 0, 0)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationPosition = R.id.nav_home_page
        binding.navView.setCheckedItem(navigationPosition)

        val headerView = binding.navView.getHeaderView(0)
        headerView.txtUserName.text = userSession!!.getUsername()
        headerView.txtAppVersion.text = userSession!!.getEmail()
        loadFragment(HomePageFragment())
    }


    fun loadFragment(fragment: Fragment){
        navigationPosition=fragment.id
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.addToBackStack(null)
        transaction.commit()
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    override fun onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }

        if (navigationPosition == R.id.nav_home_page) {
            finish()
        } else {
            loadFragment(HomePageFragment())
            binding.navView.setCheckedItem(navigationPosition)
        }
    }


}
