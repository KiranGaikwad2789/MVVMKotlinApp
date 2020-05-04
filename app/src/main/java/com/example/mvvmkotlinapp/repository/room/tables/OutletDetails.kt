package com.example.mvvmkotlinapp.repository.room.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.mvvmkotlinapp.repository.room.Outlet

@Entity(foreignKeys = arrayOf(
    ForeignKey(entity = Outlet::class,
        parentColumns = arrayOf("outlet_id"),
        childColumns = arrayOf("outlet_id"),
        onDelete = ForeignKey.CASCADE)
))
data class OutletDetails(@PrimaryKey(autoGenerate = true) val uid: Int?,
                         @ColumnInfo(name = "outlet_id") var outlet_id: Int?=0,
                         @ColumnInfo(name = "outlet_name") var outlet_name: String? =null,
                         @ColumnInfo(name = "route_name") var route_name: String? =null,
                         @ColumnInfo(name = "dist_name") var dist_name: String? =null,
                         @ColumnInfo(name = "outlet_type") var outlet_type: String? =null,
                         @ColumnInfo(name = "outlet_subtype") var outlet_subtype: String? =null,
                         @ColumnInfo(name = "outlet_address") var outlet_address: String? =null,
                         @ColumnInfo(name = "outlet_owner_name") var outlet_owner_name: String? =null,
                         @ColumnInfo(name = "outlet_contact_number") var outlet_contact_number: String? =null,
                         @ColumnInfo(name = "outlet_contact_alt_number") var outlet_contact_alt_number: String? =null,
                         @ColumnInfo(name = "outlet_email_id") var outlet_email_id: String? =null,
                         @ColumnInfo(name = "outlet_contact_person") var outlet_contact_person: String? =null,
                         @ColumnInfo(name = "outlet_sizeof_facility") var outlet_sizeof_facility: String? =null,
                         @ColumnInfo(name = "outlet_GST_number") var outlet_GST_number: String? =null,
                         @ColumnInfo(name = "outlet_license_details") var outlet_license_details: String? =null,
                         @ColumnInfo(name = "outlet_business_monthly") var outlet_business_monthly: String? =null,
                         @ColumnInfo(name = "outlet_day_footprint") var outlet_day_footprint: String? =null,
                         @ColumnInfo(name = "outlet_competitor_remarks") var outlet_competitor_remarks: String? =null,
                         @ColumnInfo(name = "outlet_date_of_established") var outlet_date_of_established: String? =null,
                         @ColumnInfo(name = "outlet_business_segment") var outlet_business_segment: String? =null,
                         @ColumnInfo(name = "outlet_primary_business") var outlet_primary_business: String? =null,
                         @ColumnInfo(name = "outlet_status") var outlet_status: String? =null) {

    constructor() : this(0,
        outlet_name =null,
        route_name = null,
        dist_name =null,
        outlet_type =null,
        outlet_subtype =null,
        outlet_address =null,
        outlet_owner_name = null,
        outlet_contact_number =null,
        outlet_contact_alt_number = null,
        outlet_email_id = null,
        outlet_contact_person = null,
        outlet_sizeof_facility =null,
        outlet_GST_number =null,
        outlet_license_details =null,
        outlet_business_monthly = null,
        outlet_day_footprint =null,
        outlet_competitor_remarks = null,
        outlet_date_of_established = null,
        outlet_business_segment = null,
        outlet_primary_business =null,
        outlet_status =null)

}