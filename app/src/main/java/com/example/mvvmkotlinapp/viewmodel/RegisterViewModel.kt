package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.model.RegisterUserModel
import com.example.mvvmkotlinapp.repository.LoginRepository
import com.example.mvvmkotlinapp.repository.room.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


class RegisterViewModel(application: Application) : AndroidViewModel(application) {

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


    private var repository: LoginRepository = LoginRepository(application)
    public var status = MutableLiveData<Boolean?>()
    private val disposable = CompositeDisposable()


    fun onLoginClicked(user: User) {

        //val user = User(0,userName.value, userMobileNumber.value, userAddress.value,userEmail.value,userPassword.value)
        Log.e("register usernmae",""+user.username)
        if (user.toString().equals(null)) {
            errorUserName.setValue("Enter UserName")
        } else {
            errorUserName.setValue(null)
        }

        if (user.equals("null")) {
            erroMobileNumber.setValue("Please enter mobile number")
        } else if (isContactNoLengthTen()) {
            erroMobileNumber.setValue("Please enter 10 digit mobile number")
        }else {
            erroMobileNumber.setValue(null)
        }

        if (user.toString().equals("null")) {
            errorUserAddress.setValue("Enter address")
        } else {
            errorUserAddress.setValue(null)
        }

        if (isValidEmail(userEmail.toString())) {
            errorUserEmail.setValue("Enter correct email-id.")
        } else {
            errorUserEmail.setValue(null)
        }

        if (userPassword.toString().equals("null")) {
            errorUserPassword.setValue("Enter password")
        } else {
            errorUserPassword.setValue(null)
        }

        if (user != null) {
            insertNewUser(user)
        }
    }

    fun insertNewUser(student: User) {
        disposable.add(
            repository.insertNewUser(student)
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableSingleObserver<Long>() {
                    override fun onSuccess(t: Long) {
                        Log.e("ViewModel Insert", t.toString())
                        status.postValue(true)
                    }
                    override fun onError(e: Throwable) {
                        Log.e("ViewModel error", e.message)
                        status.postValue(false)
                    }

                })!!
        )
    }



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
        } else userName.toString()
    }

    fun getContactNumber(): String? {
        return if (userMobileNumber == null) {
            ""
        } else userMobileNumber.toString()
    }

    fun getStreetDetails(): String? {
        return if (userAddress.toString() == "null") {
            ""
        } else userAddress.toString()
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }





}