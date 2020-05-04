package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.repository.CustomerProfileRepository
import com.example.mvvmkotlinapp.repository.room.tables.OutletDetails
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


class CustomerProfileViewModel(application: Application) : AndroidViewModel(application)  {


    val errorOutletName: MutableLiveData<String> = MutableLiveData()
    val errorRouteName: MutableLiveData<String> = MutableLiveData()
    val errorContactNumber: MutableLiveData<String> = MutableLiveData()
    val errorContactAltNumber: MutableLiveData<String> = MutableLiveData()
    val errorOutletEmailID: MutableLiveData<String> = MutableLiveData()
    val errorOutletGSTNumber: MutableLiveData<String> = MutableLiveData()

    private var repository: CustomerProfileRepository = CustomerProfileRepository(application)
    public var updateStatus = MutableLiveData<String?>()
    private val disposable = CompositeDisposable()
    var outletType: String? = null
    var outletSubType: String? = null



    private val context = application.applicationContext

    fun getOutletDetails(outletId: String) = repository.getOutletDetails(outletId)

    fun onSelectItem(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if (parent != null) {

            if(parent.getId() == R.id.spnOutletType) {
                outletType= parent?.selectedItem.toString()
                Log.e("outlet type ",""+outletType)
            } else if(parent.getId() == R.id.spnOutletSubType)
            {
                outletSubType= parent?.selectedItem.toString()
                Log.e("outlet subtype ",""+outletSubType)
            } else {
                Log.e("outlet else ","else")
            }


        }
    }

    fun onOutletUpdateClicked(outletDetails: OutletDetails) {

        outletDetails.outlet_type=outletType
        outletDetails.outlet_subtype=outletSubType

        Log.e("outlet button click",""+outletDetails.toString())

        if (outletDetails.outlet_name.equals("")) {
            errorOutletName.setValue("Enter Outlet Name")
        } else {
            errorOutletName.setValue(null)
        }
        if (outletDetails.route_name.equals("")) {
            errorRouteName.setValue("Enter Route Name")
        } else {
            errorRouteName.setValue(null)
        }

        if (!outletDetails.outlet_name.equals("") && !outletDetails.route_name.equals("")) {
            Log.e("outlet details viewmodel for insert: ",""+ outletDetails)

            if(outletDetails.uid==null){
                insertOutletDetails(outletDetails)
            }else{
                updateOutletDetails(outletDetails)
            }
        }
    }

    fun insertOutletDetails(outletDetails: OutletDetails) {
        disposable.add(
            repository.insertOutletDetails(outletDetails)
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableSingleObserver<Long>() {
                    override fun onSuccess(t: Long) {
                        Log.e("Outlet Insert ", t.toString())
                        updateStatus.postValue("1")
                    }
                    override fun onError(e: Throwable) {
                        Log.e("ViewModel error", e.message)
                        updateStatus.postValue(e.message)
                    }

                })!!
        )
    }

    fun updateOutletDetails(outletDetails: OutletDetails) {
        disposable.add(
            repository.updateOutletDetails(outletDetails)
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableSingleObserver<Int>() {
                    override fun onSuccess(t: Int) {
                        Log.e("outlet update ", t.toString())
                        updateStatus.postValue("1")
                    }
                    override fun onError(e: Throwable) {
                        Log.e("ViewModel error", e.message)
                        updateStatus.postValue(e.message)
                    }

                })!!
        )
    }

}