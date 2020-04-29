package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.repository.ProductListRepository
import com.example.mvvmkotlinapp.repository.room.tables.MasterProductOrder
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

class ProductListViewModel(application: Application) : AndroidViewModel(application),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private var repository: ProductListRepository = ProductListRepository(application)
    private val disposable = CompositeDisposable()
    public var resultMasterProductOrder = MutableLiveData<Long?>()




    fun getProductCatList() = repository.getProductCatList()

    fun getProductList() = repository.getProductList()

    fun getSelectedProductList() = repository.getSelectedProductList()

    // Product Order
    fun deleteProductTable()=repository.deleteProductOrderTable()

    //insert product order
    fun insertSelectedProducts(arrayList: ArrayList<ProductOrderModel>?){

        for (product in arrayList!!) {
            insertSelectedProducts1(product)
            repository.updateProductSelectedQuantity(product.product_id,true, product.product_quantity)
        }
    }

    //fun addNewMasterProductOrder(masterProductOrder:MasterProductOrder) = repository.addNewMasterProductOrder(masterProductOrder)



    fun addNewMasterProductOrder(
        masterProductOrder: MasterProductOrder,
        arryListProductCart: ArrayList<ProductOrderModel>?
    ): MutableLiveData<Long?> {

        disposable.add(repository.addNewMasterProductOrder(masterProductOrder)
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableSingleObserver<Long>() {
                    override fun onSuccess(masterOrderID: Long) {
                        var status="Pending"
                        //repository.updateProductMasterProductID(arryListProductCart,masterOrderID?.toInt(),status)
                        //updateProductMasterProductID(arryListProductCart, masterOrderID?.toInt(),status)

                        resultMasterProductOrder.postValue(masterOrderID)
                    }
                    override fun onError(e: Throwable) {
                        resultMasterProductOrder.postValue(0)
                        Log.e("ViewModel product error", e.message)
                    }
                })!!
        )
        return resultMasterProductOrder
    }

    fun updateProductMasterProductID(arryListProductCart: ArrayList<ProductOrderModel>?, it: Int?, status: String) =repository.updateProductMasterProductID(arryListProductCart,it,status)



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

    fun updateProductSelectedQuantity(product_id: Int?, product_isSelected: Boolean?, product_quantity: Int?) =repository.updateProductSelectedQuantity(product_id,product_isSelected,product_quantity)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

        }
        return false
    }


}