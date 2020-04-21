package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.app.PendingIntent.getActivity
import android.util.Log
import android.widget.CompoundButton
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.model.NewOrderModel
import com.example.mvvmkotlinapp.repository.CaptureOutletRepository
import com.example.mvvmkotlinapp.repository.room.Outlet
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.fragmets.HomePageFragment
import com.example.mvvmkotlinapp.view.fragmets.homefragments.NewOrderAddProductFragment
import com.example.mvvmkotlinapp.view.fragmets.homefragments.NewOrderFragment
import io.reactivex.disposables.CompositeDisposable


class NewOrderViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private var repository: CaptureOutletRepository = CaptureOutletRepository(application)
    private val disposable = CompositeDisposable()
    public var getOutlets = MutableLiveData<List<Outlet>>()

    val errorDistName: MutableLiveData<String> = MutableLiveData()
    val errorRouteName: MutableLiveData<String> = MutableLiveData()
    val errorOutletName: MutableLiveData<String> = MutableLiveData()

    //private val orderModel: MutableLiveData<NewOrderModel>? = null
    private var activity1: NewOrderFragment? =null

    fun anotherClass(fragment: NewOrderFragment) { // Save instance of main class for future use
        activity1 = fragment
    }

    fun onNewOrderClicked(order:NewOrderModel){

        if (order.routeName=="")
            errorRouteName.value="Please select Route"
        else
            errorRouteName.value=null

        if (order.outletName=="")
            errorOutletName.value="Please select Outlet"
        else
            errorOutletName.value=null

        if (order.distributorName=="")
            errorDistName.value="Please select Distributor"
        else
            errorDistName.value=null



        if (!order.routeName.equals("") && !order.outletName.equals("") &&
            !order.distributorName.equals("")) {
            Log.e("order1 : ", ""+order.routeName)
            Log.e("order2 : ", ""+order.outletName)
            Log.e("order3 : ", ""+order.distributorName)

            activity1!!.loadFragment(NewOrderAddProductFragment())
        }
    }


    fun getRouteList() = repository.getRouteList()

    fun getOutletList() = repository.getOutletList()

    fun getDistList() = repository.getDistList()

    fun onCheckedChange(button: CompoundButton?, check: Boolean) {
        Log.e("chkbox", "onCheckedChange: $check")
    }


    /*@RequiresApi(Build.VERSION_CODES.O)
    fun onCheckedChanged(checked: Boolean) { // implementation
        Log.e("switch ischeck: ", "" + checked)
    }*/


}