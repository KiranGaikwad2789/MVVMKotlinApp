package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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

    private var registerUserModelLiveData: MutableLiveData<RegisterUserModel>? = null

    private var repository: LoginRepository = LoginRepository(application)

    var status = MutableLiveData<Boolean?>()
    private val disposable = CompositeDisposable()



    /*fun insertStudent(student: User) {

        disposable.add(
            repository.insertStudent(student)
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


    }*/



    fun onLoginClicked() {
        Log.e("register Viewmodel","enter")
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

            //user?.let { insertStudent(it) }

            user?.let { insertUserRecord(it) }

        }
    }

    //User Record insert
    fun insertUserRecord(user:User){
        repository.insertUser(user)
    }

    fun getInsertResult(): Long? {
        return repository.getInsertResult()
    }


    suspend fun addRecipesDetails(cookingRecipes: User): Long {
        return repository.addRecipesData(cookingRecipes)
    }

}