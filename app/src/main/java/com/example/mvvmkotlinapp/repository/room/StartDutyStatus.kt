package com.example.mvvmkotlinapp.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "StartDutyStatus")
data class StartDutyStatus(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "status") val status: String?,
    @ColumnInfo(name = "time") val time: String?
)