package com.example.mvvmkotlinapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.APIInterface
import com.example.mvvmkotlinapp.model.LoginInfo
import com.example.mvvmkotlinapp.repository.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class LoginRepository {

    companion object{
        var mInstance: LoginRepository? = null
        public fun getmInstance(): LoginRepository? {
            if (mInstance == null) {
                mInstance = LoginRepository()
            }
            return mInstance
        }

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

}