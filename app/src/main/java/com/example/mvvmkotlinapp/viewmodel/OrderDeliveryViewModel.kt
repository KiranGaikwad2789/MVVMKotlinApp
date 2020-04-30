package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.repository.OrderDeliveryRpository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class OrderDeliveryViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: OrderDeliveryRpository = OrderDeliveryRpository(application)
    private val disposable = CompositeDisposable()
    public var resultMasterProductOrder = MutableLiveData<Int?>()


    fun getOrderDeliveryList(orderStatus:String,orderStatus1:String) = repository.getOrderDeliveryList(orderStatus,orderStatus1)

    fun getOutletNameFromID(outeletId:String) = repository.getOutletNameFromID(outeletId)

    fun getDistributorNameFromID(distId:String) = repository.getDistributorNameFromID(distId)

    fun getOrderedProductList(masterProductOrderId:Int) = repository.getOrderedProductList(masterProductOrderId)

    fun updateShortCloseDeliveredQuantity(arryListProducts:List<ProductOrderModel>) =repository.updateShortCloseDeliveredQuantity(arryListProducts)


    fun updateMasterProductOrderToDeliver(uid: Int, OrderDeliveredDate: String, orderStatus: String, orderTotalQuantity: Int?,order_total_price:Double?): MutableLiveData<Int?> {

        disposable.add(repository.updateMasterProductOrderToDeliver(uid, OrderDeliveredDate, orderStatus, orderTotalQuantity,order_total_price)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeWith(object : DisposableSingleObserver<Int>() {
                override fun onSuccess(masterOrderID: Int) {
                    resultMasterProductOrder.setValue(masterOrderID)
                }
                override fun onError(e: Throwable) {
                    resultMasterProductOrder.setValue(0)
                    Log.e("ViewModel product error", e.message)
                }
            })!!
        )
        return resultMasterProductOrder
    }

}