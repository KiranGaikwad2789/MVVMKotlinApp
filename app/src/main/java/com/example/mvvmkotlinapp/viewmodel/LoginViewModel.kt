package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.repository.LoginRepository
import com.example.mvvmkotlinapp.repository.room.*
import com.example.mvvmkotlinapp.repository.room.tables.Product
import com.example.mvvmkotlinapp.repository.room.tables.ProductCategory
import com.example.mvvmkotlinapp.view.activities.RegisterActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class LoginViewModel(application: Application) : AndroidViewModel(application){

    private val context = application.applicationContext

    val erroMobileNumber: MutableLiveData<String> = MutableLiveData()
    val errorUserPassword: MutableLiveData<String> = MutableLiveData()

    private var repository: LoginRepository = LoginRepository(application)
    private val disposable = CompositeDisposable()
    public var getUser = MutableLiveData<User?>()


    fun onLoginClicked(user: User) {

        Log.e("register usernmae",""+user)

        if (user.mobilenumber.equals("")) {
            erroMobileNumber.setValue("Please enter mobile number")
        } else if (isContactNoLengthTen(user.mobilenumber)) {
            erroMobileNumber.setValue("Please enter 10 digit mobile number")
        }else {
            erroMobileNumber.setValue(null)
        }

        if (user.password.equals("")) {
            errorUserPassword.setValue("Enter password")
        } else {
            errorUserPassword.setValue(null)
        }

        if (!user.mobilenumber.equals("") && !user.password.equals("")) {
            getUserRecord(user)
        }
    }

    fun onRegisterClick(){
        var intent= Intent(context, RegisterActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent)
    }


    fun getUserRecord(user: User) {
        disposable.add(repository.getUserRecord(user)
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableSingleObserver<User>() {

                    override fun onSuccess(user: User) {
                        user.success="1"
                        Log.e("ViewModel login", user.toString())
                        getUser.postValue(user)
                    }
                    override fun onError(e: Throwable) {
                        user.success="0"
                        Log.e("ViewModel error", e.message)
                        getUser.postValue(null)
                    }
                })!!
        )
    }

    fun isContactNoLengthTen(mobilenumber: String?): Boolean {
        return getContactNumber(mobilenumber)!!.length < 10
    }

    fun getContactNumber(mobilenumber: String?): String? {
        return if (mobilenumber.equals("")) {
            ""
        } else mobilenumber.toString()
    }


    fun insertFeatureList(arrayList: ArrayList<Features>){
        repository.insertFeatures(arrayList)
    }

    fun deleteFeatureTable()=repository.deleteFeatureTable()


    //city only insert
    fun insertCityListOnly(
        arrayList: ArrayList<City>){
        repository.insertCityOnly(arrayList)
    }

    //city and Feature Insert
    fun insertCityList(
        arrayList: ArrayList<City>,
        arrayListFeaturesInfo: ArrayList<Features>,
        arrayListRoute: ArrayList<Route>,
        arrayListOutlet: ArrayList<Outlet>,
        arrayListDistributor: ArrayList<Distributor>,
        arrayListProductCat: ArrayList<ProductCategory>,
        arrayListProduct: ArrayList<Product>
    ){
        repository.insertCity(arrayList,arrayListFeaturesInfo,arrayListRoute,arrayListOutlet,
            arrayListDistributor,arrayListProductCat,arrayListProduct)
    }

    fun getCityList() = repository.getCityList()

    fun deleteCityTable()=repository.deleteCityble()

    // Route
    fun deleteRouteTable()=repository.deleteRouteTable()

    // Outlet
    fun deleteOutletTable()=repository.deleteOutletTable()

    // Dist
    fun deleteDistTable()=repository.deleteDistTable()

    // Product Cat
    fun deleteProductCatTable()=repository.deleteProductCatTable()

    // Product Cat
    fun deleteProductTable()=repository.deleteProductTable()



}