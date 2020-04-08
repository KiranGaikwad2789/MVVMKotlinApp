package com.example.mvvmkotlinapp.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "City")
class City (

    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "cityId") val cityId: String?,
    @ColumnInfo(name = "cityName") val cityName: String?
)