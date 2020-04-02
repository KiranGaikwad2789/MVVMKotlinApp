package com.example.mvvmkotlinapp.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmkotlinapp.model.LoginInfo

class LoginViewModel: ViewModel() {

    val EmailAddress: MutableLiveData<String> = MutableLiveData()
    val Password: MutableLiveData<String> = MutableLiveData()

    private var userMutableLiveData: MutableLiveData<LoginInfo?>? = null

    fun getUser(): MutableLiveData<LoginInfo?>? {
        if (userMutableLiveData == null) {
            userMutableLiveData = MutableLiveData()
        }
        return userMutableLiveData
    }

    fun onClick(view: View?) {
        val loginUser = LoginInfo(EmailAddress.value!!, Password.value!!)
        userMutableLiveData!!.setValue(loginUser)
    }

}