package com.example.mvvmkotlinapp.model

class NewOrderModel(
    var routeName: String? =null,
    var outletName: String? =null,
    var distributorName: String? =null
) {

    constructor() : this(
        routeName = null, outletName = null,
        distributorName = null)

    override fun toString(): String {
        return routeName+", "+outletName+", "+distributorName
    }

}