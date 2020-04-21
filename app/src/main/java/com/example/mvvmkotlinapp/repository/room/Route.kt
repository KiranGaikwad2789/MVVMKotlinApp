package com.example.mvvmkotlinapp.repository.room

import androidx.room.*
import androidx.room.ForeignKey.CASCADE


@Entity(tableName = "Route",foreignKeys = arrayOf(ForeignKey(entity = User::class,
    parentColumns = arrayOf("user_id"),
    childColumns = arrayOf("user_id"),
    onDelete = ForeignKey.CASCADE)))
data class Route(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "route_id") var route_id: Int,
    @ColumnInfo(name = "route_name") var route_name: String? =null,
    @ColumnInfo(name = "status") var status: String? =null,
    @ColumnInfo(name = "route_lat") var route_lat: Double? =0.0,
    @ColumnInfo(name = "route_long") var route_long: Double? =0.0,
    @ColumnInfo(name = "user_id") var user_id: Int? =0) {

    constructor() : this( route_id=0,route_name = null, status = null,
        route_lat = 0.0,
        route_long = 0.0,user_id = 0)
}