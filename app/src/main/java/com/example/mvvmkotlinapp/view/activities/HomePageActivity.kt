package com.example.mvvmkotlinapp.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.BottomNavigationViewHelper
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.ActivityHomePageBinding
import com.example.mvvmkotlinapp.view.fragmets.HomePageFragment
import com.example.mvvmkotlinapp.viewmodel.HomeMainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_AUTO
import kotlinx.android.synthetic.main.app_bar_home_page.*
import kotlinx.android.synthetic.main.app_bar_home_page.view.*
import kotlinx.android.synthetic.main.layout_actionbar.view.*
import kotlinx.android.synthetic.main.nav_header_home_page.view.*


class HomePageActivity : AppCompatActivity() {//,NavigationView.OnNavigationItemSelectedListener{

    //Note: Attempting to inflate a menu with more than 5 items will crash with an IllegalStateException. To dynamically determine the max number of items, use BottomNavigationView#maxItemCount.

    //https://stackoverflow.com/questions/46872747/bottomnavigationview-on-databinding-inside-viewmodel
    //https://medium.com/@oldergod/bottomnavigationview-callback-and-and-databinding-9775b0c31d38

    //https://stackoverflow.com/questions/45205003/data-binding-error-trying-to-pass-viewmodel-into-include-layout-with-abstract-va
    //https://medium.com/@elia.maracani/android-data-binding-passing-a-variable-to-an-include-d-layout-3567099b58f


    lateinit var binding: ActivityHomePageBinding
    private var homeMainViewModel: HomeMainViewModel? = null
    private var userSession: UserSession? =null
    var navigationPosition: Int = 0
    var bottomCount=5


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
        binding.navigation.isItemHorizontalTranslationEnabled = true
        binding.navigation.labelVisibilityMode=LABEL_VISIBILITY_AUTO
        binding.navigation.maxItemCount(6)

        //BottomNavigationViewHelper.removeShiftMode(binding.navigation);
        //binding.navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);


        val headerView = binding.navView.getHeaderView(0)
        headerView.txtUserName.text = userSession!!.getUsername()
        headerView.txtAppVersion.text = userSession!!.getEmail()
        loadFragment(HomePageFragment())

        binding.actionBarLayout.action_bar_id.imgDownLoadData.setOnClickListener {
            Toast.makeText(this,"data",Toast.LENGTH_SHORT).show()
        }
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

    @SuppressLint("RestrictedApi")
    fun BottomNavigationView.disableShiftMode() {
        val menuView = getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode = menuView::class.java.getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.isAccessible = false
            for (i in 0 until menuView.childCount) {
                val item = menuView.getChildAt(i) as BottomNavigationItemView
                item.setShifting(false)
                // set once again checked value, so view will be updated
                item.setChecked(item.itemData.isChecked)
            }
        } catch (e: NoSuchFieldException) {
            Log.e("TAG", "Unable to get shift mode field", e)
        } catch (e: IllegalStateException) {
            Log.e("TAG", "Unable to change value of shift mode", e)
        }
    }
}

private operator fun Int.invoke(i: Int) {

}
