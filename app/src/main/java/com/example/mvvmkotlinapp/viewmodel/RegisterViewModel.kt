package com.example.mvvmkotlinapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmkotlinapp.model.RegisterUserModel
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.User
import com.example.mvvmkotlinapp.view.activities.RegisterActivity

class RegisterViewModel: ViewModel() {
    var appDatabase: AppDatabase? =null

    val errorUserName: MutableLiveData<String> = MutableLiveData()
    val erroMobileNumber: MutableLiveData<String> = MutableLiveData()
    val errorUserAddress: MutableLiveData<String> = MutableLiveData()
    val errorUserEmail: MutableLiveData<String> = MutableLiveData()
    val errorUserPassword: MutableLiveData<String> = MutableLiveData()


    val userName: MutableLiveData<String> = MutableLiveData()
    val userMobileNumber: MutableLiveData<String?> = MutableLiveData()
    val userAddress: MutableLiveData<String?> = MutableLiveData()
    val userEmail: MutableLiveData<String?> = MutableLiveData()
    val userPassword: MutableLiveData<String?> = MutableLiveData()

    private var registerUserModelLiveData: MutableLiveData<RegisterUserModel>? = null

    fun getUserDetails(): LiveData<RegisterUserModel?> {
        if (registerUserModelLiveData == null) {
            registerUserModelLiveData = MutableLiveData()
        }
        return registerUserModelLiveData as MutableLiveData<RegisterUserModel>
    }

    fun registerViewModel(application: RegisterActivity) {
        appDatabase = AppDatabase.getDatabase(application!!)
    }

    fun onLoginClicked() {
        Log.e("Viewmodel Enter","enter")
        val addressListModel = RegisterUserModel(userName.value, userMobileNumber.value, userAddress.value,userEmail.value,userPassword.value)

        if (addressListModel.userName.toString().equals("null")) {
            errorUserName.setValue("Enter UserName")
        } else {
            errorUserName.setValue(null)
        }


        if (addressListModel.userMobileNumber.equals("null")) {
            erroMobileNumber.setValue("Please enter mobile number")
        } else if (addressListModel.isContactNoLengthTen()) {
            erroMobileNumber.setValue("Please enter 10 digit mobile number")
        }else {
            erroMobileNumber.setValue(null)
        }


        if (addressListModel.userAddress.toString().equals("null")) {
            errorUserAddress.setValue("Enter address")
        } else {
            errorUserAddress.setValue(null)
        }


        if (addressListModel.isValidEmail(userEmail.toString())) {
            errorUserEmail.setValue("Enter correct email-id.")
        } else {
            errorUserEmail.setValue(null)
        }

        if (addressListModel.userPassword.toString().equals("null")) {
            errorUserPassword.setValue("Enter password")
        } else {
            errorUserPassword.setValue(null)
        }

        if (registerUserModelLiveData != null) {
            registerUserModelLiveData!!.setValue(addressListModel)
            var user: User? =null
            user?.username =addressListModel.userName
            user?.mobilenumber =addressListModel.userMobileNumber
            user?.address =addressListModel.userAddress
            user?.email =addressListModel.userEmail
            user?.password =addressListModel.userPassword

            val list = appDatabase!!.userDao().insertAll(user!!)

        }
    }
}