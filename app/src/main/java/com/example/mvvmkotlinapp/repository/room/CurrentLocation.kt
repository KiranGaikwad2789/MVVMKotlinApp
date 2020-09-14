package com.example.mvvmkotlinapp.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CurrentLocation")
data class CurrentLocation(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "lattitude") val lattitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "distance") val distance: Double
)