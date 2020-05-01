package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.repository.CaptureOutletRepository
import com.example.mvvmkotlinapp.repository.room.Distributor
import com.example.mvvmkotlinapp.repository.room.Outlet
import com.example.mvvmkotlinapp.repository.room.Route


class CaptureOutletViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private var repository: CaptureOutletRepository = CaptureOutletRepository(application)

    val errorRouteName: MutableLiveData<String> = MutableLiveData()
    val errorOutletName: MutableLiveData<String> = MutableLiveData()

    var strRouteName: String? =null

    var spnRouteDistList: Spinner? = null

    var route: LiveData<List<Route>>? = null
    var distributor: LiveData<List<Distributor>>? = null


    fun onOutletSaveClicked(outlet: Outlet){
        Log.e("Outlet : ", ""+outlet)
    }

    var route1:LiveData<List<Route>>? = repository.getRouteList()

    fun getRouteList() = repository.getRouteList()

    fun getOutletList() = repository.getOutletList()


    fun onSelectItem(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) { // Spinner
        if (parent != null) {

            strRouteName= parent.selectedItem.toString()
        }
        Log.e("selected value: ",""+ parent!!.selectedItem)

        //pos                                 get selected item position
        //view.getText()                      get lable of selected item
        //parent.getAdapter().getItem(pos)    get item by pos
        //parent.getAdapter().getCount()      get item count
        //parent.getCount()                   get item count
        //parent.getSelectedItem()            get selected item
        //and other...
    }

    fun onSplitTypeChanged(radioGroup: RadioGroup?, id: Int,spnRouteList:Spinner) { // Radio Group
        spnRouteDistList=spnRouteList
        val radioButtonID = radioGroup?.checkedRadioButtonId
        val radioButton = radioGroup?.findViewById(radioButtonID!!) as RadioButton
        val selectedText = radioButton.text as String
        Log.e("selected RadioGroup: ",""+ selectedText)

        if(selectedText.equals("By Route")){
            route = repository.getRouteList()!!
            Log.e("selected route: ",""+ route!!.value)
        }else if(selectedText.equals("By Distributor")){
            distributor = repository.getDistList()
            Log.e("selected distributor: ",""+ distributor!!.value)
        }

        //val arrayadapter = ArrayAdapter<Route>(context, android.R.layout.simple_spinner_item, route)
        //spnRouteDistList.
    }


}