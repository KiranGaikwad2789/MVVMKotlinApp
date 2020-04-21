package com.example.mvvmkotlinapp.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
    ForeignKey(entity = Route::class,
        parentColumns = arrayOf("route_id"),
        childColumns = arrayOf("route_id"),
        onDelete = ForeignKey.CASCADE)
))
data class Outlet(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "outlet_id") var outlet_id: Int?,
    @ColumnInfo(name = "outlet_name") var outlet_name: String? =null,
    @ColumnInfo(name = "route_id") var route_id: Int? =0,
    @ColumnInfo(name = "status") var status: String? =null,
    @ColumnInfo(name = "outlet_lat") var outlet_lat: Double? =0.0,
    @ColumnInfo(name = "outlet_long") var outlet_long: Double? =0.0) {

    constructor() : this(0, outlet_name=null,route_id=0, status = null,
        outlet_lat = 0.0,
        outlet_long = 0.0)
}