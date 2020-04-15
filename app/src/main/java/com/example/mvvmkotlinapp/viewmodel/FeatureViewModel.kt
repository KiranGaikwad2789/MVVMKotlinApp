package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.mvvmkotlinapp.repository.room.Features
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.fragmets.homefragments.*

class FeatureViewModel(application: Application) : AndroidViewModel(application) {


    private var activity1: HomePageActivity? =null

    fun anotherClass(activity: HomePageActivity) { // Save instance of main class for future use
        activity1 = activity
    }


    fun buttonClicked(features: Features) {

        Log.e("click model ","click "+features.featureId)

        if (features.featureId==1){
            activity1!!.loadFragment(CaptureOutletFragment())
        }else if(features.featureId==2){
            activity1!!.loadFragment(CreateNewLeadFragment())
        }else if(features.featureId==3){
            activity1!!.loadFragment(CustomerProfileFragment())
        }else if(features.featureId==4){
            activity1!!.loadFragment(NewOrderFragment())
        }else if(features.featureId==5){
            activity1!!.loadFragment(OrderDeliveryFragment())
        }else if(features.featureId==6){
            activity1!!.loadFragment(MyDashboardFragment())
        }else if(features.featureId==7){
            activity1!!.loadFragment(WeeklyStockFragment())
        }else if(features.featureId==8){
            activity1!!.loadFragment(ProductMenuFragment())
        }else if(features.featureId==9){
            activity1!!.loadFragment(MyDSRFragment())
        }else if(features.featureId==10){
            activity1!!.loadFragment(MyTimeSheetFragment())
        }else if(features.featureId==11){
            activity1!!.loadFragment(MyClaimsFragment())
        }else if(features.featureId==12){
            activity1!!.loadFragment(ManageLeaveFragment())
        }else if(features.featureId==13){
            activity1!!.loadFragment(CustomerSupportFragment())
        }
    }


}