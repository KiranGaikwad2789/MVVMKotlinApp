package com.example.mvvmkotlinapp.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "Features")
data class Features(
    @TypeConverters(DataConverter::class)
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "featureId") var featureId: Int?,
    @ColumnInfo(name = "featureName") var featureName: String?
){

    constructor() : this(0, featureId = 0, featureName = "null")
    constructor(i: Int, s: String) : this()
}