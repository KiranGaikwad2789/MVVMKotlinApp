package com.example.mvvmkotlinapp.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "user")
data class User(
        val userName: String="",
        @PrimaryKey
        val email: String="",
        val mobile: String="",
        val password: String=""
){

        @Ignore
        constructor() : this("", "", "", "")
}