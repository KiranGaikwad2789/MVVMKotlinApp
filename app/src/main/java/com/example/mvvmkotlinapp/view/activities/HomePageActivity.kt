package com.example.mvvmkotlinapp.view.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.ActivityHomePageBinding
import com.example.mvvmkotlinapp.view.fragmets.HomePageFragment
import com.example.mvvmkotlinapp.view.fragmets.homefragments.OrderDeliveryFragment
import com.example.mvvmkotlinapp.viewmodel.HomeMainViewModel
import com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_LABELED
import kotlinx.android.synthetic.main.app_bar_home_page.*
import kotlinx.android.synthetic.main.nav_header_home_page.view.*


class HomePageActivity : AppCompatActivity() {

    private var binding: ActivityHomePageBinding? = null
    private var homeMainViewModel: HomeMainViewModel? = null
    private var userSession: UserSession? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeBindingView()
        initializeObjects()

        loadFragment(HomePageFragment())
    }

    private fun initializeBindingView() {
        homeMainViewModel = ViewModelProviders.of(this).get(HomeMainViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page)
        binding?.lifecycleOwner = this
        binding?.homeModel=homeMainViewModel
    }

    private fun initializeObjects() {

        setSupportActionBar(toolbar)
        homeMainViewModel!!.anotherClass(this)
        userSession=UserSession(this)

        val toggle = ActionBarDrawerToggle(this, binding?.drawerLayout, toolbar, 0, 0)
        binding?.drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()

        binding!!.navigation!!.isItemHorizontalTranslationEnabled = true
        binding!!.navigation.labelVisibilityMode= LABEL_VISIBILITY_LABELED

        val headerView = binding!!.navView.getHeaderView(0)
        headerView.txtUserName.text = userSession!!.getUsername()
        headerView.txtAppVersion.text = userSession!!.getEmail()
    }




    fun loadFragment(fragment: Fragment){

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment,fragment.javaClass.simpleName)
        //transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.addToBackStack(null)
        transaction.commit()

        if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding!!.drawerLayout.closeDrawer(GravityCompat.START);
        }

    }

    override fun onBackPressed() {

        if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding!!.drawerLayout.closeDrawer(GravityCompat.START);
        }

        val myFragment: Fragment = supportFragmentManager.findFragmentByTag("HomePageFragment")!!

        val fragments: List<Fragment> = supportFragmentManager.getFragments()

        if (myFragment != null && myFragment.isVisible()) { // add your code here
            finish()
        }else {
            if (fragments != null) {
                for (fragment in fragments) {
                    Log.e("fragment ",""+fragment)
                    if (fragment != null && fragment.isVisible){
                        if ((fragment.javaClass.simpleName).equals("OrderDeliveryDetailsFragment")){
                            loadFragment(OrderDeliveryFragment())
                        }else{
                            visibleMenuItems(0)
                            loadFragment(HomePageFragment())
                        }
                    }
                }
            }
        }
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
