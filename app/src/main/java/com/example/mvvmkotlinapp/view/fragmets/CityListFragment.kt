package com.example.mvvmkotlinapp.view.fragmets

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmkotlinapp.common.RecyclerItemClickListenr
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.FragmentCityBinding
import com.example.mvvmkotlinapp.repository.room.City
import com.example.mvvmkotlinapp.view.adapter.CityListAdapter
import com.example.mvvmkotlinapp.viewmodel.CityListViewModel
import java.util.*


class CityListFragment() : DialogFragment() {


    companion object {

        fun newInstance(): CityListFragment? {
            return CityListFragment()
        }

        var cityArrayList: ArrayList<City>? = null
        var array_sort: ArrayList<City>? = null
    }
    //https://www.simplifiedcoding.net/search-functionality-recyclerview/
    //https://www.androidhive.info/2017/11/android-recyclerview-with-search-filter-functionality/
    //https://demonuts.com/kotlin-recyclerview-search-using-edittext/


    lateinit var cityListViewModel:CityListViewModel
    lateinit var cityBinding : FragmentCityBinding
    private var userSession: UserSession? =null

    private var adapter: CityListAdapter? = null
    internal var textlength = 0
    var cityRecord: City? =null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.getWindow()?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            dialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
           // dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        cityListViewModel = ViewModelProviders.of(this).get(CityListViewModel::class.java)
        cityBinding = DataBindingUtil.inflate(inflater!!, com.example.mvvmkotlinapp.R.layout.fragment_city, container, false)
        val view: View = cityBinding.getRoot()
        cityBinding.lifecycleOwner = this
        cityBinding.cityViewModel=cityListViewModel
        userSession=UserSession(activity)

        activity?.let {
            cityListViewModel?.getCityList()?.observe(it, Observer<List<City>> {
                this.setDataToAdapter(it)
            })
        }

        cityBinding.recyclerViewCityList.addOnItemTouchListener(RecyclerItemClickListenr(this!!.activity!!, cityBinding.recyclerViewCityList, object : RecyclerItemClickListenr.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {

                cityRecord= cityArrayList!!.get(position);
                Toast.makeText(context,""+ cityRecord!!.cityName,Toast.LENGTH_SHORT).show()
            }
            override fun onItemLongClick(view: View?, position: Int) {
                TODO("do nothing")
            }
        }))

        cityBinding.txtSelectCity.setOnClickListener {

            if (TextUtils.isEmpty(cityRecord?.toString())){
                Toast.makeText(activity,"Please select city",Toast.LENGTH_SHORT).show()
            }else{
                val filter = "thisIsForMyFragment"
                val intent = Intent(filter) //If you need extra, add: intent.putExtra("extra","something");
                intent.putExtra("city",cityRecord?.cityName);
                activity?.let { it1 -> LocalBroadcastManager.getInstance(it1).sendBroadcast(intent) }
                userSession!!.setCurrentCity(cityRecord?.cityName)
                Toast.makeText(activity,""+ cityRecord!!.cityName,Toast.LENGTH_SHORT).show()
                dialodDismiss()
            }

        }

        cityBinding.txtCancelDialog.setOnClickListener {
            dialodDismiss()
        }


        cityBinding.edtSearchCity!!.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textlength = cityBinding.edtSearchCity!!.text!!.length
                array_sort?.clear()
                for (i in cityArrayList!!.indices) {
                    if (textlength <= cityArrayList!![i].cityName!!.length) {
                        Log.d("ertyyy", cityArrayList!![i].cityName!!.toLowerCase().trim())
                        if (cityArrayList!![i].cityName!!.toLowerCase().trim().contains(
                                cityBinding.edtSearchCity!!.text.toString().toLowerCase().trim { it <= ' ' })
                        ) {
                            array_sort!!.add(cityArrayList!![i])
                        }
                    }
                }

                adapter = activity?.let { CityListAdapter(it, array_sort) }
                cityBinding.recyclerViewCityList!!.adapter = adapter
                cityBinding.recyclerViewCityList!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                val dividerItemDecoration = DividerItemDecoration(cityBinding.recyclerViewCityList.getContext(), LinearLayoutManager.VERTICAL)
                cityBinding.recyclerViewCityList.addItemDecoration(dividerItemDecoration)
            }
        })
        return view
    }

    private fun dialodDismiss() {

        val dialog: Dialog? = dialog
        if (dialog != null) {
            onDestroy()
            onDestroyView()
            onDetach()
            dialog.dismiss()
        }
    }

    private fun setDataToAdapter(arryListCity: List<City>) {
        cityArrayList = ArrayList<City>()
        cityArrayList!!.addAll(arryListCity)
        array_sort = ArrayList<City>()
        array_sort!!.addAll(arryListCity)

        adapter = CityListAdapter(this!!.activity!!, arryListCity)
        cityBinding.recyclerViewCityList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        cityBinding.recyclerViewCityList!!.adapter = adapter
        cityBinding.recyclerViewCityList!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)



        //val dividerItemDecoration = DividerItemDecoration(cityBinding.recyclerViewCityList.getContext(), LinearLayoutManager.VERTICAL)
        //cityBinding.recyclerViewCityList.addItemDecoration(dividerItemDecoration)
    }

}