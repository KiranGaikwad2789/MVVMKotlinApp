package com.example.mvvmkotlinapp.view.fragmets

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.DateTime
import com.example.mvvmkotlinapp.common.RecyclerItemClickListenr
import com.example.mvvmkotlinapp.databinding.FragmentHomePageBinding
import com.example.mvvmkotlinapp.receiver.AlarmReceive
import com.example.mvvmkotlinapp.repository.StartDutyRepository
import com.example.mvvmkotlinapp.repository.room.City
import com.example.mvvmkotlinapp.repository.room.Features
import com.example.mvvmkotlinapp.repository.room.StartDutyStatus
import com.example.mvvmkotlinapp.services.LocationTrackingService
import com.example.mvvmkotlinapp.view.adapter.HomePageAdapter
import com.example.mvvmkotlinapp.view.fragmets.homefragments.CaptureOutletFragment
import com.example.mvvmkotlinapp.viewmodel.HomePageViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class HomePageFragment : Fragment() {

    //https://labs.ribot.co.uk/approaching-android-with-mvvm-8ceec02d5442

    lateinit var homePageViewModel:HomePageViewModel
    lateinit var homePageBinding : FragmentHomePageBinding

    private var alarmMgr: AlarmManager? = null
    private var pendingIntent: PendingIntent? =null
    private var currentDate: DateTime? =null

    private var adapter: HomePageAdapter? =null
    private var arryListFeatures: ArrayList<Features>? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homePageViewModel = ViewModelProviders.of(this).get(HomePageViewModel::class.java)
        homePageBinding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_home_page, container, false)
        val view: View = homePageBinding.getRoot()
        homePageBinding.lifecycleOwner = this
        homePageBinding.homePageViewModel=homePageViewModel

        homePageViewModel!!.anotherClass(this)

        insertFeatures()

        activity?.let {
            homePageViewModel?.getFeatureList()?.observe(it, Observer<List<Features>> {
                this.setFeatureList(it)
                arryListFeatures= it as ArrayList<Features>?
            })
        }

        initViews()
        getStartDutyStatus()


        homePageBinding.recyclerViewFeatureList.addOnItemTouchListener(RecyclerItemClickListenr(this!!.activity!!, homePageBinding.recyclerViewFeatureList, object : RecyclerItemClickListenr.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {

                var feature= arryListFeatures!!.get(position);
                Toast.makeText(context,""+feature.featureId, Toast.LENGTH_SHORT).show()
                //do your work here..

                if (feature.featureId==1){
                    loadFragment(CaptureOutletFragment())
                }
            }
            override fun onItemLongClick(view: View?, position: Int) {
                TODO("do nothing")
            }
        }))


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


    private fun setFeatureList(arryListFeatures: List<Features>?) {
        Log.e("FeatyreList size : ",""+ arryListFeatures!!.size)
        adapter = activity?.let { HomePageAdapter(it,arryListFeatures) }!!
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.stackFromEnd = true
        homePageBinding.recyclerViewFeatureList.adapter = adapter
        homePageBinding.recyclerViewFeatureList.layoutManager = GridLayoutManager(context, 3)

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


    private fun insertFeatures() {

        val feature1 = Features(0,1, "Capture Outlet")
        val feature2 = Features(0,2, "New Lead")
        val feature3 = Features(0,3, "Customer Profile")
        val feature4 = Features(0,4, "New order")
        val feature5 = Features(0,5, "Order Delivery")
        val feature6 = Features(0,6, "My Dashboard")
        val feature7 = Features(0,7, "Weekly Stock")
        val feature8 = Features(0,8, "Product menu")
        val feature9 = Features(0,9, "My DSR")
        val feature10 = Features(0,10, "My TimeSheet")
        val feature11 = Features(0,11, "My Claims")
        val feature12 = Features(0,12, "Manage Leave")
        val feature13 = Features(0,13, "Customer Support")

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
        homePageViewModel.deleteTable()
        homePageViewModel.insertFeatureList(arrayListFeaturesInfo)

        val c1 = City(0,1, "Pune")
        val c2 = City(0,2, "Mumbai")
        val c3 = City(0,3, "Nashik")
        val c4 = City(0,4, "Dhule")
        val c5 = City(0,5, "Nandurbar")
        val c6 = City(0,6, "Nagpur")
        val c7 = City(0,7, "Jalgaon")
        var arrayListCity= ArrayList<City>()

        if (arrayListCity != null) {
            arrayListCity!!.add(c1)
            arrayListCity!!.add(c2)
            arrayListCity!!.add(c3)
            arrayListCity!!.add(c4)
            arrayListCity!!.add(c5)
            arrayListCity!!.add(c6)
            arrayListCity!!.add(c7)
        }

        homePageViewModel.deleteCityTable()
        homePageViewModel.insertCityList(arrayListCity)

    }

    fun loadFragment(fragment: Fragment){
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
