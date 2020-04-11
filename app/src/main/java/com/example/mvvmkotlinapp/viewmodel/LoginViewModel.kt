package com.example.mvvmkotlinapp.viewmodel

import android.text.Editable
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmkotlinapp.databinding.ActivityLoginBinding
import com.example.mvvmkotlinapp.model.LoginInfo
import com.example.mvvmkotlinapp.repository.LoginRepository

class LoginViewModel: ViewModel() {

    public var username: MutableLiveData<String>? = null
    public var password: MutableLiveData<String>? = null

    public var usernameError: MutableLiveData<Editable>? = null
    public var passwordError: MutableLiveData<Editable>? = null

    private var loginLiveData: MutableLiveData<LoginInfo>? = null


    fun init() {
        username = MutableLiveData()
        password = MutableLiveData()

        usernameError = MutableLiveData()
        passwordError = MutableLiveData()
    }

    fun setUsername(username: Editable, password: Editable) {
        this.usernameError!!.value = username
        this.passwordError!!.value = password
    }

    fun doLogin() {
        loginLiveData = LoginRepository.getmInstance()!!.loginRequest(username!!.value!!, password!!.value!!)
    }

    fun getLoginData(): LiveData<LoginInfo?>? {
        return loginLiveData
    }

}