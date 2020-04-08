package com.example.mvvmkotlinapp.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Features")
data class Features(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "featureId") var featureId: String?,
    @ColumnInfo(name = "featureName") var featureName: String?
)