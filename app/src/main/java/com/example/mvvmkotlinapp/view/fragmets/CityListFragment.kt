package com.example.mvvmkotlinapp.view.fragmets

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.databinding.FragmentCityBinding
import com.example.mvvmkotlinapp.repository.room.City
import com.example.mvvmkotlinapp.viewmodel.CityListViewModel


class CityListFragment() : DialogFragment() {
    
    //https://www.androidhive.info/2017/11/android-recyclerview-with-search-filter-functionality/
    //https://demonuts.com/kotlin-recyclerview-search-using-edittext/
    constructor(arryListCity: List<City>) : this(){
        this.arryListCity=arryListCity
    }

    lateinit var cityListViewModel:CityListViewModel
    lateinit var cityBinding : FragmentCityBinding

    var recyclerViewFeatureList: ListView? = null
    var searchCity: SearchView? = null
    var adapter: ArrayAdapter<City>? = null
    var arryListCity: List<City>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        cityListViewModel = ViewModelProviders.of(this).get(CityListViewModel::class.java)
        cityBinding = DataBindingUtil.inflate(inflater!!, com.example.mvvmkotlinapp.R.layout.fragment_city, container, false)
        val view: View = cityBinding.getRoot()
        cityBinding.lifecycleOwner = this
        cityBinding.cityViewModel=cityListViewModel

        //CREATE AND SET ADAPTER TO LISTVIEW
        //CREATE AND SET ADAPTER TO LISTVIEW
        adapter = arryListCity?.let {
            ArrayAdapter<City>(this!!.activity!!, R.layout.simple_list_item_1,
                it
            )
        }
        cityBinding.recyclerViewFeatureList!!.setAdapter(adapter)



        return view
    }

}