package com.example.mvvmkotlinapp.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.interfaces.APIInterface
import com.example.mvvmkotlinapp.model.LoginInfo
import com.example.mvvmkotlinapp.repository.retrofit.RetrofitInstance
import com.example.mvvmkotlinapp.repository.room.*
import io.reactivex.Single
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

public class LoginRepository (application: Application){

    private var userDao: UserDao?=null
    private var cityDao: CityDao? = null
    private var featureDao: FeatureDao? = null
    var appDatabase: AppDatabase? =null

    init {
        appDatabase = AppDatabase.getDatabase(application)
        userDao = appDatabase?.userDao()
        cityDao = appDatabase?.cityDao()
        featureDao = appDatabase?.featureDao()
    }


    fun insertNewUser(student: User): Single<Long>? {
        return Single.fromCallable(
            Callable<Long> { userDao!!.insertNewUser(student) }
        )
    }

    fun getUserRecord(user: User): Single<User>? {
        return Single.fromCallable(
            Callable<User> { userDao!!.findByMobileNo(user.mobilenumber!!, user.password!!) }
        )
    }

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

    fun insertFeatures(arrayListFeaturesInfo: ArrayList<Features>) {
        doAsync {
            if (arrayListFeaturesInfo != null) {
                for (features in arrayListFeaturesInfo!!) {
                    featureDao!!.insertAll(features)
                }
            }
        }
    }

    fun deleteFeatureTable(){
        doAsync {
            featureDao!!.deleteTableAllEntry()
        }
    }


    //City and feature module
    fun insertCity(arrayListCity: ArrayList<City>, arrayListFeaturesInfo: ArrayList<Features>) {
        doAsync {
            if (arrayListCity != null) {
                for (city in arrayListCity!!) {
                    cityDao!!.insertAll(city)
                }
            }
            if (arrayListFeaturesInfo != null) {
                for (features in arrayListFeaturesInfo!!) {
                    featureDao!!.insertAll(features)
                }
            }
        }
    }

    fun getCityList() = cityDao?.getCityList()

    fun deleteCityble(){
        doAsync {
            cityDao!!.deleteTableAllEntry()
        }
    }





}