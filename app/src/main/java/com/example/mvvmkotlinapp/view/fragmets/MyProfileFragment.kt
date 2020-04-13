package com.example.mvvmkotlinapp.view.fragmets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.FragmentHomePageBinding
import com.example.mvvmkotlinapp.databinding.FragmentMyProfileBinding
import com.example.mvvmkotlinapp.model.LoginInfo
import com.example.mvvmkotlinapp.repository.room.CurrentLocation
import com.example.mvvmkotlinapp.view.adapter.CurrentLocationAdapter
import com.example.mvvmkotlinapp.viewmodel.CurrentLocationViewModel
import com.example.mvvmkotlinapp.viewmodel.HomePageViewModel
import com.example.mvvmkotlinapp.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.activity_login.*


class MyProfileFragment : Fragment() {

    lateinit var profileViewModel:ProfileViewModel
    lateinit var myProfileBinding : FragmentMyProfileBinding

    private var userSession: UserSession? =null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        myProfileBinding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_my_profile, container, false)
        val view: View = myProfileBinding.getRoot()
        myProfileBinding.lifecycleOwner = this
        myProfileBinding.profileiewModel=profileViewModel

        userSession=UserSession(activity)

        myProfileBinding.txtUserName.text= userSession!!.getUsername()
        myProfileBinding.txtUserContact.text= userSession!!.getMobile()
        myProfileBinding.txtUserEmail.text= userSession!!.getEmail()

        return view
    }

}
