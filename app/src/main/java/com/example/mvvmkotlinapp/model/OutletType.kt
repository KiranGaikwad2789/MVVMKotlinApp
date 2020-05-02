package com.example.mvvmkotlinapp.model

class OutletType(var outletTypeId:Int,var outletTypeName:String) {

    override fun toString(): String {
        return outletTypeName
    }
}