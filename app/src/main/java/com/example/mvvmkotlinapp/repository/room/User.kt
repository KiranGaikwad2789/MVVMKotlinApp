package com.example.mvvmkotlinapp.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.internal.LinkedTreeMap

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "username") var username: String?,
    @ColumnInfo(name = "mobilenumber") var mobilenumber: String?,
    @ColumnInfo(name = "address") var address: String?,
    @ColumnInfo(name = "email") var email: String?,
    @ColumnInfo(name = "password") var password: String?){
}