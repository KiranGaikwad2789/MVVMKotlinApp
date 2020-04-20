package com.example.mvvmkotlinapp.repository.room

import androidx.annotation.IntegerRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM User WHERE mobilenumber LIKE :mobilenumber AND " + "password LIKE :password")
    fun findByMobileNo(mobilenumber: String, password: String):User

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertNewUser(users: User): Long

    @Delete
    fun delete(user: User)

}