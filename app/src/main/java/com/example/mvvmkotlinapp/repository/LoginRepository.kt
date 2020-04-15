package com.example.mvvmkotlinapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.interfaces.APIInterface
import com.example.mvvmkotlinapp.model.LoginInfo
import com.example.mvvmkotlinapp.repository.retrofit.RetrofitInstance
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.User
import com.example.mvvmkotlinapp.repository.room.UserDao
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class LoginRepository (application: Application){

    private var userDao: UserDao?=null
    var appDatabase: AppDatabase? =null
    private var insertResult: LiveData<Integer>? = null

    init {
        appDatabase = AppDatabase.getDatabase(application)
        userDao = appDatabase?.userDao()
    }

    //Register user inser
    fun insertUser(user: User) {

        doAsync {
            insertResult=userDao!!.insertAll(user)
        }
    }

    fun getInsertResult(): LiveData<Integer>? {
        return insertResult
    }


    fun getUserRecord():LiveData<User> = userDao!!.getAll()

    fun loginRequest(username: String, password: String): MutableLiveData<LoginInfo> {

        val loginData: MutableLiveData<LoginInfo> = MutableLiveData()

        val apiInterface: APIInterface = RetrofitInstance.getClient()!!.create(APIInterface::class.java)

        val call: Call<LoginInfo>? = apiInterface.doLogin(username, password)

        call!!.enqueue(object : Callback<LoginInfo> {

            override fun onResponse(call: Call<LoginInfo>, response: Response<LoginInfo>) {
                loginData.setValue(response.body())
            }

            override fun onFailure(call: Call<LoginInfo>, t: Throwable) {
                loginData.setValue(null)
            }
        })

        return loginData
    }





}