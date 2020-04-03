package com.example.mvvmkotlinapp.viewmodel

import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmkotlinapp.model.LoginInfo
import com.example.mvvmkotlinapp.repository.LoginRepository
import com.google.android.material.textfield.TextInputLayout

class LoginViewModel: ViewModel() {

    public var username: MutableLiveData<String>? = null
    public var password: MutableLiveData<String>? = null

    private var loginLiveData: MutableLiveData<LoginInfo>? = null


    fun init() {
        username = MutableLiveData()
        password = MutableLiveData()
    }

    fun setUsername(username: String?, password: String?) {
        this.username!!.value = username
        this.password!!.value = password
    }

    fun doLogin() {
        loginLiveData = LoginRepository.getmInstance()!!.loginRequest(username!!.value!!,
            password!!.value!!
        )
    }

    fun getLoginData(): LiveData<LoginInfo?>? {
        return loginLiveData
    }


    val formErrors = ObservableArrayList<FormErrors>()

    enum class FormErrors {
        MISSING_NAME,
        INVALID_EMAIL,
        INVALID_PASSWORD,
        PASSWORDS_NOT_MATCHING,
    }


    @BindingAdapter("app:errorText")
    fun setErrorMessage(view: TextInputLayout, errorMessage: String) {
        view.error = errorMessage
    }

    fun isFormValid(): Boolean {
        formErrors.clear()
        if (username!!.value?.isNullOrEmpty()!!) {
            formErrors.add(FormErrors.MISSING_NAME)
        }
        // all the other validation you require
        return formErrors.isEmpty()
    }

}