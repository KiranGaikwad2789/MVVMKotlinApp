package com.example.mvvmkotlinapp.model

import androidx.room.*
import com.example.mvvmkotlinapp.repository.room.*
import com.example.mvvmkotlinapp.repository.room.tables.Product
import com.example.mvvmkotlinapp.repository.room.tables.ProductCategory

@Entity(tableName = "ProductOrderModel",foreignKeys = arrayOf(
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
@TypeConverters(ProductListConverter::class)
data class ProductOrderModel(@PrimaryKey(autoGenerate = true) val uid: Int,
                             @ColumnInfo(name = "master_product_orderid") var master_product_orderid: Int? =0,
                        @ColumnInfo(name = "product_id") var product_id: Int? =0,
                        @ColumnInfo(name = "product_name") var product_name: String? =null,
                        @ColumnInfo(name = "prod_cat_id") var prod_cat_id: Int? =0,
                        @ColumnInfo(name = "route_id") var route_id: Int? =0,
                        @ColumnInfo(name = "outlet_id") var outlet_id: Int? =0,
                        @ColumnInfo(name = "dist_id") var dist_id: Int? =0,
                        @ColumnInfo(name = "product_price") var product_price: Double? =0.0,
                        @ColumnInfo(name = "product_total_price") var product_total_price: Double? =0.0,
                        @ColumnInfo(name = "product_quantity") var product_quantity: Int? =0,
                        @ColumnInfo(name = "product_compony") var product_compony: String? =null,
                        @ColumnInfo(name = "status") var status: String? =null,
                        @ColumnInfo(name = "product_delivered_quantity") var product_delivered_quantity: Int? =0) {

    constructor() : this(0,0,product_id=0,product_name = null,prod_cat_id=0,route_id=0,outlet_id=0,dist_id=0, product_price = 0.0,product_total_price=0.0,
        product_quantity = 0,
        product_compony = null,
        status = null,product_delivered_quantity=0)
}