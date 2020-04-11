package com.example.mvvmkotlinapp.repository.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type


class DataConverter:Serializable {


    @TypeConverter // note this annotation
    fun fromOptionValuesList(optionValues: List<Features?>?): String? {
        if (optionValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Features?>?>() {}.type
        return gson.toJson(optionValues, type)
    }

    @TypeConverter // note this annotation
    fun toOptionValuesList(optionValuesString: String?): List<Features>? {
        if (optionValuesString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Features?>?>() {}.type
        return gson.fromJson<List<Features>>(optionValuesString, type)
    }
}