package com.example.mvvmkotlinapp.view.fragmets

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.RecyclerItemClickListenr
import com.example.mvvmkotlinapp.databinding.FragmentCityBinding
import com.example.mvvmkotlinapp.repository.room.City
import com.example.mvvmkotlinapp.view.adapter.CityListAdapter
import com.example.mvvmkotlinapp.viewmodel.CityListViewModel
import java.util.*


class CityListFragment() : DialogFragment() {


    companion object {
        lateinit var cityArrayList: ArrayList<City>
        lateinit var array_sort: ArrayList<City>
    }
    //https://www.simplifiedcoding.net/search-functionality-recyclerview/
    //https://www.androidhive.info/2017/11/android-recyclerview-with-search-filter-functionality/
    //https://demonuts.com/kotlin-recyclerview-search-using-edittext/


    lateinit var cityListViewModel:CityListViewModel
    lateinit var cityBinding : FragmentCityBinding

    //var arryListCity: List<City>? = null
    private var adapter: CityListAdapter? = null
    internal var textlength = 0
    var cityRecord: City? =null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        cityListViewModel = ViewModelProviders.of(this).get(CityListViewModel::class.java)
        cityBinding = DataBindingUtil.inflate(inflater!!, com.example.mvvmkotlinapp.R.layout.fragment_city, container, false)
        val view: View = cityBinding.getRoot()
        cityBinding.lifecycleOwner = this
        cityBinding.cityViewModel=cityListViewModel

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
                Toast.makeText(activity,""+ cityRecord!!.cityName,Toast.LENGTH_SHORT).show()
            }

        }

        cityBinding.txtCancelDialog.setOnClickListener {
            getActivity()!!.getFragmentManager().popBackStack();
        }


        cityBinding.edtSearchCity!!.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textlength = cityBinding.edtSearchCity!!.text.length
                array_sort.clear()
                for (i in cityArrayList.indices) {
                    if (textlength <= cityArrayList[i].cityName!!.length) {
                        Log.d("ertyyy", cityArrayList[i].cityName!!.toLowerCase().trim())
                        if (cityArrayList[i].cityName!!.toLowerCase().trim().contains(
                                cityBinding.edtSearchCity!!.text.toString().toLowerCase().trim { it <= ' ' })
                        ) {
                            array_sort.add(cityArrayList[i])
                        }
                    }
                }

                adapter = activity?.let { CityListAdapter(it, array_sort) }
                cityBinding.recyclerViewCityList!!.adapter = adapter
                cityBinding.recyclerViewCityList!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            }
        })
        return view
    }

    private fun setDataToAdapter(arryListCity: List<City>) {
        cityArrayList = ArrayList<City>()
        cityArrayList.addAll(arryListCity)
        array_sort = ArrayList<City>()
        array_sort.addAll(arryListCity)

        adapter = CityListAdapter(this!!.activity!!, arryListCity)
        cityBinding.recyclerViewCityList!!.adapter = adapter
        cityBinding.recyclerViewCityList!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }
}