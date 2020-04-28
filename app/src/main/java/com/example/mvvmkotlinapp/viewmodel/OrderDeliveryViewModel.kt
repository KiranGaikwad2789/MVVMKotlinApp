package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mvvmkotlinapp.repository.OrderDeliveryRpository

class OrderDeliveryViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: OrderDeliveryRpository = OrderDeliveryRpository(application)

    fun getOrderDeliveryList() = repository.getOrderDeliveryList()

    fun getOutletNameFromID(outeletId:String) = repository.getOutletNameFromID(outeletId)

    fun getDistributorNameFromID(distId:String) = repository.getDistributorNameFromID(distId)

    fun getOrderedProductList(masterProductOrderId:Int) = repository.getOrderedProductList(masterProductOrderId)



}