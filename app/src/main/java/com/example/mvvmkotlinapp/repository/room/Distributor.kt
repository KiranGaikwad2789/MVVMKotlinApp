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
class Distributor(@PrimaryKey(autoGenerate = true)
                  @ColumnInfo(name = "dist_id") var dist_id: Int?,
                  @ColumnInfo(name = "dist_name") var dist_name: String? =null,
                  @ColumnInfo(name = "route_id") var route_id: Int? =0,
                  @ColumnInfo(name = "status") var status: String? =null,
                  @ColumnInfo(name = "dist_lat") var dist_lat: Double? =0.0,
                  @ColumnInfo(name = "dist_long") var dist_long: Double? =0.0) {

    constructor() : this( dist_id = 0, dist_name=null,route_id=0, status = null,
        dist_lat = 0.0,
        dist_long = 0.0)
}