package com.example.mvvmkotlinapp.model

import android.text.TextUtils
import android.util.Patterns

class RegisterUserModel(userName: String?,userMobileNumber: String?, userAddress: String?, userEmail: String?, userPassword: String?) {

    var userName:String= userName.toString()
    var userMobileNumber:String= userMobileNumber.toString()
    var userAddress:String= userAddress.toString()
    var userEmail:String= userEmail.toString()
    var userPassword:String= userPassword.toString()


    fun isEmptyContactPersonField(): Boolean {
        return TextUtils.isEmpty(getContactPerson())
    }

    fun isContactNoLengthTen(): Boolean {
        return getContactNumber()!!.length < 10
    }

    fun isEmptyStreetField(): Boolean {
        return TextUtils.isEmpty(getStreetDetails())
    }

    fun getContactPerson(): String? {
        return if (userName == null) {
            ""
        } else userName
    }

    fun getContactNumber(): String? {
        return if (userMobileNumber == null) {
            ""
        } else userMobileNumber
    }

    fun getStreetDetails(): String? {
        return if (userAddress == "null") {
            ""
        } else userAddress
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

}