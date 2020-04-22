package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.repository.HomePageRepository
import com.example.mvvmkotlinapp.repository.ProductListRepository
import com.example.mvvmkotlinapp.repository.room.City

class ProductListViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: ProductListRepository = ProductListRepository(application)

    fun getProductCatList() = repository.getProductCatList()

    fun getProductList() = repository.getProductList()

    // Product Order
    fun deleteProductTable()=repository.deleteProductOrderTable()

    //insert product order
    fun insertProductTable(
        arrayList: ArrayList<ProductOrderModel>){
        repository.insertProductTable(arrayList)
    }

}