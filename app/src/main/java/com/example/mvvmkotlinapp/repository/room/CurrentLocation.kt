package com.example.mvvmkotlinapp.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mvvmkotlinapp.common.DateTime
import com.example.mvvmkotlinapp.repository.room.converters.TimestampConverter

@Entity(tableName = "CurrentLocation")
data class CurrentLocation(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "lattitude") val lattitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "distance") val distance: Double,
    @ColumnInfo(name = "date_time") val date_time: String,
    @ColumnInfo(name = "location_type") val location_type: String
)