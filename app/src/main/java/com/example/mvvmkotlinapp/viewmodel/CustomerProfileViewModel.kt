package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.repository.CustomerProfileRepository
import com.example.mvvmkotlinapp.repository.room.tables.OutletDetails
import io.reactivex.disposables.CompositeDisposable

class CustomerProfileViewModel(application: Application) : AndroidViewModel(application)  {


    val errorOutletName: MutableLiveData<String> = MutableLiveData()
    val errorRouteName: MutableLiveData<String> = MutableLiveData()
    val errorContactNumber: MutableLiveData<String> = MutableLiveData()
    val errorContactAltNumber: MutableLiveData<String> = MutableLiveData()
    val errorOutletEmailID: MutableLiveData<String> = MutableLiveData()
    val errorOutletGSTNumber: MutableLiveData<String> = MutableLiveData()

    private var repository: CustomerProfileRepository = CustomerProfileRepository(application)
    public var updateValidationStatus = MutableLiveData<String?>()
    private val disposable = CompositeDisposable()

    private val context = application.applicationContext

    fun getOutletDetails(outletId: String) = repository.getOutletDetails(outletId)

    fun onOutletUpdateClicked(outletDetails: OutletDetails) {
        Log.e("outlet details",""+outletDetails.toString())

        if (outletDetails.outlet_name.equals("")) {
            errorOutletName.setValue("Enter Outlet Name")
        } else {
            errorOutletName.setValue(null)
        }
        if (outletDetails.route_id==0) {
            errorRouteName.setValue("Enter Route Name")
        } else {
            errorRouteName.setValue(null)
        }
    }
}