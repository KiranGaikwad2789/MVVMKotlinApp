package com.example.mvvmkotlinapp.repository.room.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.mvvmkotlinapp.repository.room.Distributor
import com.example.mvvmkotlinapp.repository.room.Outlet
import com.example.mvvmkotlinapp.repository.room.Route
import java.io.Serializable

@Entity(tableName = "MasterProductOrder",foreignKeys = arrayOf(
    ForeignKey(entity = Route::class,
        parentColumns = arrayOf("route_id"),
        childColumns = arrayOf("route_id"),
        onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Outlet::class,
        parentColumns = arrayOf("outlet_id"),
        childColumns = arrayOf("outlet_id"),
        onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Distributor::class,
        parentColumns = arrayOf("dist_id"),
        childColumns = arrayOf("dist_id"),
        onDelete = ForeignKey.CASCADE)
))
data class MasterProductOrder(@PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "cart_ids") var cart_ids: String? =null,
    @ColumnInfo(name = "route_id") var route_id: Int? =0,
    @ColumnInfo(name = "outlet_id") var outlet_id: Int? =0,
    @ColumnInfo(name = "dist_id") var dist_id: Int? =0,
    @ColumnInfo(name = "order_total_price") var order_total_price: Double? =0.0,
    @ColumnInfo(name = "order_total_quantity") var order_total_quantity: Int? =0,
    @ColumnInfo(name = "order_date") var order_date: String? =null,
    @ColumnInfo(name = "order_status") var order_status: String? =null,
    @ColumnInfo(name = "order_total_products") var order_total_products: Int? =0,
    @ColumnInfo(name = "order_deliver_date") var order_deliver_date: String? =null,
    @ColumnInfo(name = "order_remark") var order_remark: String? =null,
    @ColumnInfo(name = "offer_id") var offer_id: Int? =0,
    @ColumnInfo(name = "offer_amount") var offer_amount: Double? =0.0,
    @ColumnInfo(name = "order_delivered_quantity") var order_delivered_quantity: Int? =0) : Serializable {

    constructor() : this(0,cart_ids=null,route_id=0,outlet_id=0,dist_id=0, order_total_price = 0.0,
        order_total_quantity = 0, order_date=null,order_status = null,order_total_products=0,order_deliver_date=null,
        order_remark=null,offer_id=0,offer_amount=0.0,order_delivered_quantity=0)
}