package com.example.mvvmkotlinapp.repository.room

import androidx.room.TypeConverter
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type

class ProductListConverter: Serializable {


    @TypeConverter
    fun listToJson(value: List<ProductOrderModel>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<ProductOrderModel>::class.java).toList()
}