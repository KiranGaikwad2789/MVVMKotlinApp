package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.repository.CaptureOutletRepository
import com.example.mvvmkotlinapp.repository.HomePageRepository
import com.example.mvvmkotlinapp.repository.room.Outlet
import com.example.mvvmkotlinapp.repository.room.Route
import com.example.mvvmkotlinapp.repository.room.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class CaptureOutletViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private var repository: CaptureOutletRepository = CaptureOutletRepository(application)
    private val disposable = CompositeDisposable()
    public var getOutlets = MutableLiveData<List<Outlet>>()




    val errorRouteName: MutableLiveData<String> = MutableLiveData()
    val errorOutletName: MutableLiveData<String> = MutableLiveData()


    fun onOutletSaveClicked(outlet: Outlet){
        Log.e("Outlet : ", ""+outlet)
    }


    fun getRouteList() = repository.getRouteList()

    fun getOutletList() = repository.getOutletList()


    /*fun getOutletList(route_id:String):List<Outlet>? {
        Log.e("Oultet list2: ",""+ repository.getOutletList(route_id))
        return repository.getOutletList(route_id)
    }*/


    /*fun getOutletList(route_id:String): MutableLiveData<List<Outlet>> {
        disposable.add(repository.getOutletList(route_id)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeWith(object : DisposableSingleObserver<List<Outlet>>() {

                override fun onSuccess(list: List<Outlet>) {
                    getOutlets.postValue(list)
                    Log.e("outlet list: ",""+list.size)
                }
                override fun onError(e: Throwable) {
                    getOutlets.postValue(null)
                }
            })!!
        )
        return getOutlets
    }*/

}