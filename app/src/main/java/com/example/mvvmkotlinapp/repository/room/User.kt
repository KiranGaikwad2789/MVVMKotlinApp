package com.example.mvvmkotlinapp.repository.room

import androidx.room.*
import com.example.mvvmkotlinapp.model.MasterResponseModel

@Entity(indices = [Index(value = ["mobilenumber"], unique = true)])
//@Entity(tableName = "User",indices = arrayOf(Index(value = ["mobilenumber", "email"], unique = true)))
//@Entity(foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["_id"], childColumns = ["labelId"], onDelete = ForeignKey.CASCADE)], indices = [Index(value = ["labelId", "taskId"], unique = true)])
data class User(
    //@PrimaryKey(autoGenerate = true) val uid: Int=0,
    @ColumnInfo(name = "username") var username: String? =null,
    @ColumnInfo(name = "mobilenumber") var mobilenumber: String? =null,
    @ColumnInfo(name = "address") var address: String? =null,
    @ColumnInfo(name = "email") var email: String? =null,
    @ColumnInfo(name = "password") var password: String? =null,
    @ColumnInfo(name = "city") var city: String? =null):MasterResponseModel(){

    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

        constructor() : this( username = null, mobilenumber = null,
            address = null,
            email = null,
            password = null, city=null)
}
