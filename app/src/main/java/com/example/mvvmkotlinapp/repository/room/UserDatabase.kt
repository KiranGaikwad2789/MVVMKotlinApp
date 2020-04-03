package com.example.mvvmkotlinapp.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvvmkotlinapp.model.User


@Database(entities = arrayOf(User::class), version = 1,exportSchema = false)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}