package com.example.mvvmkotlinapp.view.fragmets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.FragmentMyProfileBinding
import com.example.mvvmkotlinapp.model.LoginInfo
import com.example.mvvmkotlinapp.repository.room.CurrentLocation
import com.example.mvvmkotlinapp.view.adapter.CurrentLocationAdapter
import com.example.mvvmkotlinapp.viewmodel.CurrentLocationViewModel
import kotlinx.android.synthetic.main.activity_login.*


/**
 * A simple [Fragment] subclass.
 */
class MyProfileFragment : Fragment() {

    private val currentLocationViewModel: CurrentLocationViewModel? = null
    private val currentLocationAdapter: CurrentLocationAdapter? = null


    private var myProfileDataBinding: FragmentMyProfileBinding? =null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        myProfileDataBinding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_my_profile, container, false)
        val view: View = myProfileDataBinding?.getRoot()!!

        getAllLocations()

        return view
    }

    private fun getAllLocations() {
        if (currentLocationViewModel != null) {

           /* currentLocationViewModel.getLoginData()!!.observe(this!!.activity!!, Observer { btnLogin })

            currentLocationViewModel!!.getAllLocations(this!!.activity!!).observe(activity,
                Observer<List<CurrentLocation?>?>()  { loginModel ->
                    if (loginModel != null) {

                    }
                })*/
        }
    }
//https://download.androidwave.com/login
    //https://androidwave.com/android-data-binding-recyclerview/
}
