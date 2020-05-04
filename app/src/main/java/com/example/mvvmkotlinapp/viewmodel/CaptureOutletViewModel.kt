package com.example.mvvmkotlinapp. viewmodel

import android.R
import android.app.Application
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.model.NewOrderModel
import com.example.mvvmkotlinapp.repository.CaptureOutletRepository
import com.example.mvvmkotlinapp.repository.room.Distributor
import com.example.mvvmkotlinapp.repository.room.Outlet
import com.example.mvvmkotlinapp.repository.room.Route
import com.example.mvvmkotlinapp.view.adapter.OutletListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


class CaptureOutletViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private var repository: CaptureOutletRepository = CaptureOutletRepository(application)
    private val disposable = CompositeDisposable()

    val errorOutletName: MutableLiveData<String> = MutableLiveData()
    val errorRouteName: MutableLiveData<String> = MutableLiveData()

    var spnRouteDistList: Spinner? = null
    var autoCompleteOutletName: AutoCompleteTextView? = null
    var radioGroupType: String = "By Route"
    var strSpinnerName: String? =null
    val nextFragmentNavigate = MutableLiveData<NewOrderModel>()


    val route: LiveData<List<Route>>? = repository.getRouteList()

    fun getRouteList() = repository.getRouteList()

    fun getOutletList() = repository.getOutletList()

    fun onSplitTypeChanged(radioGroup: RadioGroup?, id: Int,spnRouteList:Spinner,autoCompleteOutlet:AutoCompleteTextView) { // Radio Group

        spnRouteDistList=spnRouteList
        autoCompleteOutletName=autoCompleteOutlet

        val radioButtonID = radioGroup?.checkedRadioButtonId
        val radioButton = radioGroup?.findViewById(radioButtonID!!) as RadioButton
        radioGroupType = radioButton.text as String

        if(radioGroupType.equals("By Route")){
            getAllRoutesList(spnRouteDistList!!)
        }else if(radioGroupType.equals("By Distributor")){
            getAllDistributorList(spnRouteDistList!!)
        }
    }

    fun onSelectItem(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) { // Spinner
        if (parent != null) {

            strSpinnerName= parent.selectedItem.toString()

            var spinnerValNID=parent.selectedItem.toString().split(" | ")
            Log.e("selected spinner name: ",""+spinnerValNID[0])
            Log.e("selected spinner id: ",""+ spinnerValNID[1])
            getOutletListFromID(spinnerValNID[1],radioGroupType)
        }
    }

    fun getOutletListFromID(
        spinnerValueID: String,
        radioGroupType: String
    ){
        disposable.add(
            repository.getOutletListFromID(spinnerValueID,radioGroupType)
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableSingleObserver<List<Outlet>>() {
                    override fun onSuccess(t: List<Outlet>) {

                        if (t != null) {
                            val adapter = OutletListAdapter(context, R.layout.simple_list_item_1, t)
                            autoCompleteOutletName?.setAdapter(adapter)
                            autoCompleteOutletName?.threshold = 2
                        }

                        //Listners
                       /* autoCompleteOutletName?.setOnItemClickListener() { parent,view, position, id ->
                            val selectedPoi = parent.adapter.getItem(position) as Outlet?
                            autoCompleteOutletName!!.setText(selectedPoi?.outlet_name+" | "+selectedPoi?.outlet_id)
                            Log.e("selected outlet ", selectedPoi.toString())
                        }*/
                    }
                    override fun onError(e: Throwable) {
                        Log.e("selected route error", e.message)
                    }
                })!!
        )
    }



    fun getAllRoutesList(spnRouteDistList: Spinner){
        disposable.add(
            repository.getAllRoutesList()
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableSingleObserver<List<Route>>() {
                    override fun onSuccess(t: List<Route>) {
                        val aa = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, t)
                        spnRouteDistList.adapter=aa
                    }
                    override fun onError(e: Throwable) {
                        Log.e("selected route error", e.message)
                    }
                })!!
        )
    }

    fun getAllDistributorList(spnRouteDistList: Spinner){
        disposable.add(
            repository.getAllDistributorsList()
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableSingleObserver<List<Distributor>>() {
                    override fun onSuccess(t: List<Distributor>) {
                        val aa = ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item, t)
                        spnRouteDistList.adapter=aa
                    }
                    override fun onError(e: Throwable) {
                        Log.e("selected route error", e.message)
                    }

                })!!
        )
    }

    fun onNewOrderClicked(order:NewOrderModel){
        Log.e("Outlet selected: ", ""+ order.outletName)
        Log.e("spinner selected : ", ""+ strSpinnerName)

        var newOrderModel = NewOrderModel(strSpinnerName,order.outletName,null)
        nextFragmentNavigate.postValue(newOrderModel)
    }

    fun onOutletSaveClicked(outlet: Outlet){
        Log.e("Outlet : ", ""+outlet)
    }
}