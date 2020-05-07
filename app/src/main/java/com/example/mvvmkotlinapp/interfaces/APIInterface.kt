package com.example.mvvmkotlinapp.interfaces

import com.example.mvvmkotlinapp.model.LoginInfo
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface APIInterface {

    @FormUrlEncoded
    @POST("auth/login/")
    fun doLogin(@Field("username") username: String?, @Field("password") password: String?): Call<LoginInfo>?

    @GET("users.json")
    fun getChatUsersList(): Call<JSONObject>?

    @GET("users.json")
    fun getCliente(): Call<JsonObject>?
}