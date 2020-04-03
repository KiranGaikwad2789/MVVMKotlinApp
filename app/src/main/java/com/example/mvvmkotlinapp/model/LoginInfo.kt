package com.example.mvvmkotlinapp.model

import android.util.Patterns
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import com.google.android.material.textfield.TextInputLayout

public class LoginInfo(val eMailID: String, val password: String) {

    private var strEmailAddress: String? = null
    private var strPassword: String? = null
    public var status:String?=null

    fun LoginUser(EmailAddress: String, Password: String) {
        strEmailAddress = EmailAddress
        strPassword = Password
    }

    public fun getStrEmailAddress(): String? {
        return strEmailAddress
    }

    fun getStrPassword(): String {
        return strPassword!!
    }

    fun isEmailValid(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(getStrEmailAddress()).matches()
    }


    fun isPasswordLengthGreaterThan5(): Boolean {
        return getStrPassword().length > 5
    }



}

