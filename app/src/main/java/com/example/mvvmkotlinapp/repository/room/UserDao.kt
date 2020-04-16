package com.example.mvvmkotlinapp.repository.room

import androidx.annotation.IntegerRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM User")
    fun getAll(): LiveData<User>

    @Query("SELECT * FROM User WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM User WHERE mobilenumber LIKE :mobilenumber AND " + "password LIKE :password LIMIT 1")
    fun findByName(mobilenumber: String, password: String): List<User>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAll(users: User): Long

    @Delete
    fun delete(user: User)

}