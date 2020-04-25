package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.repository.ProductListRepository
import com.example.mvvmkotlinapp.utils.AlertDialog
import com.example.mvvmkotlinapp.view.fragmets.AlarmSettingFragment
import com.example.mvvmkotlinapp.view.fragmets.HomePageFragment
import com.example.mvvmkotlinapp.view.fragmets.MyProfileFragment
import com.example.mvvmkotlinapp.view.fragmets.ReviewOrderFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ProductListViewModel(application: Application) : AndroidViewModel(application),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private var repository: ProductListRepository = ProductListRepository(application)
    private val disposable = CompositeDisposable()



    fun getProductCatList() = repository.getProductCatList()

    fun getProductList() = repository.getProductList()

    fun getSelectedProductList() = repository.getSelectedProductList()

    // Product Order
    fun deleteProductTable()=repository.deleteProductOrderTable()

    //insert product order
    fun insertSelectedProducts(arrayList: ArrayList<ProductOrderModel>?){

        for (product in arrayList!!) {
            insertSelectedProducts1(product)
        }
    }

    public var status = MutableLiveData<String?>()

    fun insertSelectedProducts1(user: ProductOrderModel) {
        disposable.add(
            repository.insertNewUser(user)
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableSingleObserver<Long>() {
                    override fun onSuccess(t: Long) {
                        Log.e("v Product insert", t.toString())
                        status.postValue("1")
                    }
                    override fun onError(e: Throwable) {
                        Log.e("ViewModel product error", e.message)
                        status.postValue(e.message)
                    }

                })!!
        )
    }

    fun updateProductCart(productSelected: ProductOrderModel) =repository.updateProductCart(productSelected)

    fun removeProductCart(productCartId: Int) =repository.removeProductCart(productCartId)


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

        }
        return false
    }


}