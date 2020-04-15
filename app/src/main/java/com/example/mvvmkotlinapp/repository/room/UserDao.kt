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

    @Query("SELECT * FROM User WHERE username LIKE :username AND " + "password LIKE :password LIMIT 1")
    fun findByName(username: String, password: String): List<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(users: User): LiveData<Integer>? = null

    @Delete
    fun delete(user: User)

}