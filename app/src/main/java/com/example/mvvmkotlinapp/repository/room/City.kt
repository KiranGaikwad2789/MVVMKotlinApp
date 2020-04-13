package com.example.mvvmkotlinapp.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "City")
class City (

    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "cityId") val cityId: Int?,
    @ColumnInfo(name = "cityName") val cityName: String?
){

    constructor() : this(0, cityId = 0, cityName = "null")
    //constructor(i: Int, s: String) : this()


    override fun toString(): String {
        return cityName.toString()
    }
}
