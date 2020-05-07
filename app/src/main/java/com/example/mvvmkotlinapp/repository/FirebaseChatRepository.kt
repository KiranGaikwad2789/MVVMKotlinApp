package com.example.mvvmkotlinapp.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.interfaces.APIInterface
import com.example.mvvmkotlinapp.repository.retrofit.RetrofitInstance
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FirebaseChatRepository(application: Application) {

    /*fun getChatUsersList(): MutableLiveData<String>? {
        val data: MutableLiveData<String>? = MutableLiveData()

        RetrofitInstance.getClient()?.create(APIInterface::class.java)?.getChatUsersList()
            ?.enqueue(object : Callback<String> {

                override fun onResponse(call: Call<String>?, response: Response<String>?) {
                    data?.postValue(response?.body())
                    Log.e("Retrofit response repository  ", response?.body())
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("Retrofit response error  ",t.message)
                    data?.postValue(null)
                }
            })
        return data
    }*/

    fun getlist() : MutableLiveData<JsonObject>?{

        val data: MutableLiveData<JsonObject>? = MutableLiveData()


        val call: Call<JsonObject>? = RetrofitInstance.getClient()?.create(APIInterface::class.java)?.getCliente()
        call?.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                if (response.isSuccessful) { // your code to get data from the list
                    data?.postValue(response.body())
                    Log.e("Retrofit response repository  ", response?.body().toString())
                } else {

                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                data?.postValue(null)
                Log.e("Retrofit response error  ",t.message)
            }
        })
        return data
    }


}