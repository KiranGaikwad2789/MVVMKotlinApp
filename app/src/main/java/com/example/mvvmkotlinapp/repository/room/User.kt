package com.example.mvvmkotlinapp.repository.room

import androidx.room.*
import com.example.mvvmkotlinapp.model.MasterResponseModel

@Entity(indices = [Index(value = ["mobilenumber"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "user_id") val user_id: Int=0,
    @ColumnInfo(name = "username") var username: String? =null,
    @ColumnInfo(name = "mobilenumber") var mobilenumber: String? =null,
    @ColumnInfo(name = "address") var address: String? =null,
    @ColumnInfo(name = "email") var email: String? =null,
    @ColumnInfo(name = "password") var password: String? =null,
    @ColumnInfo(name = "city") var city: String? =null,
    @ColumnInfo(name = "IMEI") var IMEI: String? =null,
    @ColumnInfo(name = "androidID") var androidID: String? =null):MasterResponseModel(){

        constructor() : this( 0,username = null, mobilenumber = null, address = null, email = null, password = null, city=null,IMEI=null,androidID=null)
}
