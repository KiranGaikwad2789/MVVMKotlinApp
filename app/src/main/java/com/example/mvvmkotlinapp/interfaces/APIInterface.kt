package com.example.mvvmkotlinapp.interfaces

import com.example.mvvmkotlinapp.model.LoginInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIInterface {

    @FormUrlEncoded
    @POST("auth/login/")
    fun doLogin(@Field("username") username: String?, @Field("password") password: String?): Call<LoginInfo>?

}