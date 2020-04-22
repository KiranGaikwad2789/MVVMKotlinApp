package com.example.mvvmkotlinapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.mvvmkotlinapp.repository.room.Distributor
import com.example.mvvmkotlinapp.repository.room.Outlet
import com.example.mvvmkotlinapp.repository.room.Route
import com.example.mvvmkotlinapp.repository.room.tables.Product
import com.example.mvvmkotlinapp.repository.room.tables.ProductCategory

@Entity(foreignKeys = arrayOf(
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
        onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = ProductCategory::class,
        parentColumns = arrayOf("prod_cat_id"),
        childColumns = arrayOf("prod_cat_id"),
        onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Product::class,
        parentColumns = arrayOf("product_id"),
        childColumns = arrayOf("product_id"),
        onDelete = ForeignKey.CASCADE)
))
data class ProductOrderModel(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "product_order_id") var product_order_id: Int?,
                        @ColumnInfo(name = "product_id") var product_id: Int? =0,
                        @ColumnInfo(name = "product_name") var product_name: String? =null,
                        @ColumnInfo(name = "prod_cat_id") var prod_cat_id: Int? =0,
                        @ColumnInfo(name = "route_id") var route_id: Int? =0,
                        @ColumnInfo(name = "outlet_id") var outlet_id: Int? =0,
                        @ColumnInfo(name = "dist_id") var dist_id: Int? =0,
                        @ColumnInfo(name = "product_price") var product_price: Double? =0.0,
                        @ColumnInfo(name = "product_quantity") var product_quantity: Int? =0,
                        @ColumnInfo(name = "product_compony") var product_compony: String? =null,
                        @ColumnInfo(name = "status") var status: String? =null) {


}