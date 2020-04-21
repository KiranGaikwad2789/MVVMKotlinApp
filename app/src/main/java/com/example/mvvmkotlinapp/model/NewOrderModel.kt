package com.example.mvvmkotlinapp.model

class NewOrderModel(
    var routeName: String? =null,
    var outletName: String? =null,
    var distributorName: String? =null,
    var isCheck: Boolean
) {

    constructor() : this(
        routeName = null, outletName = null,
        distributorName = null, isCheck = false)

}