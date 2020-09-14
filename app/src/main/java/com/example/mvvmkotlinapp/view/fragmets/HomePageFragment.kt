package com.example.mvvmkotlinapp.view.fragmets

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.FragmentHomePageBinding
import com.example.mvvmkotlinapp.interfaces.DrawerLocker
import com.example.mvvmkotlinapp.repository.room.CurrentLocation
import com.example.mvvmkotlinapp.repository.room.Features
import com.example.mvvmkotlinapp.repository.room.StartDutyStatus
import com.example.mvvmkotlinapp.view.adapter.HomePageAdapter
import com.example.mvvmkotlinapp.viewmodel.HomePageViewModel


class HomePageFragment : Fragment() {

    //https://labs.ribot.co.uk/approaching-android-with-mvvm-8ceec02d5442

    lateinit var homePageViewModel:HomePageViewModel
    lateinit var homePageBinding : FragmentHomePageBinding
    private var adapter: HomePageAdapter? =null
    private var userSession: UserSession? =null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homePageViewModel = ViewModelProviders.of(this).get(HomePageViewModel::class.java)
        homePageBinding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_home_page, container, false)
        val view: View = homePageBinding.getRoot()
        homePageBinding.lifecycleOwner = this
        homePageBinding.homePageViewModel=homePageViewModel
        homePageViewModel.init()

        initializeObjects()

        activity?.let {
            homePageViewModel?.getFeatureList()?.observe(it, Observer<List<Features>> {
                this.setFeatureList(it)
            })
        }

        activity?.let {
            homePageViewModel?.getStartDutyStatus()?.observe(it, Observer<StartDutyStatus> {

                if (it != null) {
                    if (!TextUtils.isEmpty(it.toString()) || it!=null){
                        if (it.status.equals("ON")){
                            homePageBinding.switchStartDuty.isChecked=true
                        }else if (it.status.equals("OFF")){
                            homePageBinding.switchStartDuty.isChecked=false
                        }
                    }
                }
            })
        }


        activity?.let {
            homePageViewModel?.getAllLocation()?.observe(it, Observer<List<CurrentLocation>> {
                Log.e("CurrentLocation list: ",""+it)
            })
        }

        return view
    }

    private fun initializeObjects() {
        homePageViewModel!!.anotherClass(this)
        userSession=UserSession(activity)
        homePageBinding.txtLocation.text= userSession!!.getCurrentCity()

        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as DrawerLocker).setDrawerEnabled(true)

    }





    private fun setFeatureList(arryListFeatures: List<Features>?) {

        adapter = activity?.let { HomePageAdapter(it,arryListFeatures) }!!
        homePageBinding.recyclerViewFeatureList.adapter = adapter
        homePageBinding.recyclerViewFeatureList.layoutManager = GridLayoutManager(context, 3)
        homePageBinding.recyclerViewFeatureList.setNestedScrollingEnabled(false);

    }


    override fun onDestroyView() {
        super.onDestroyView()
        homePageViewModel.onDestroyView()
    }


    fun loadFragment(fragment: Fragment, s: String){

        if(s.equals("dialog")){
            val newFragment: CityListFragment? = CityListFragment.Companion.newInstance()
            if (newFragment != null) {
                activity?.supportFragmentManager?.let { newFragment.show(it, "dialog") }
            }
        }else{
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.container, fragment)
            transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

    }

    fun showDialog(distance: Double) {

        val builder = activity?.let { AlertDialog.Builder(it) }
        builder?.setTitle("Today's Total Distance.")
        builder?.setMessage("Dear user your total distance from Start-Duty to End-Duty is "+distance+" KM.")
        builder?.setCancelable(false)
        builder?.setPositiveButton(android.R.string.yes) { dialog, which ->

            //homePageViewModel?.deleteLocationTable()

            dialog.dismiss()
        }
        builder?.show()
    }

}
