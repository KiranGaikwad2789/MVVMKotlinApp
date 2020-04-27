package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.repository.OrderDeliveryRpository
import com.example.mvvmkotlinapp.repository.room.Outlet
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class OrderDeliveryViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: OrderDeliveryRpository = OrderDeliveryRpository(application)

    fun getOrderDeliveryList() = repository.getOrderDeliveryList()

    fun getOutletNameFromID(outeletId:String) = repository.getOutletNameFromID(outeletId)

    fun getDistributorNameFromID(distId:String) = repository.getDistributorNameFromID(distId)

}