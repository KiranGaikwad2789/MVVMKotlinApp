package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.repository.LoginRepository
import com.example.mvvmkotlinapp.repository.room.City
import com.example.mvvmkotlinapp.repository.room.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    val errorUserName: MutableLiveData<String> = MutableLiveData()
    val erroMobileNumber: MutableLiveData<String> = MutableLiveData()
    val errorUserAddress: MutableLiveData<String> = MutableLiveData()
    val errorUserEmail: MutableLiveData<String> = MutableLiveData()
    val errorUserPassword: MutableLiveData<String> = MutableLiveData()

    val userName: MutableLiveData<String> = MutableLiveData()
    val userMobileNumber: MutableLiveData<String?> = MutableLiveData()
    val userAddress: MutableLiveData<String?> = MutableLiveData()
    val userEmail: MutableLiveData<String?> = MutableLiveData()
    val userPassword: MutableLiveData<String?> = MutableLiveData()


    private var repository: LoginRepository = LoginRepository(application)
    public var status = MutableLiveData<String?>()
    private val disposable = CompositeDisposable()

    val city: LiveData<List<City>>? = repository.getCityList()
    var strCityName: String? =null

    fun onSelectItem(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if (parent != null) {
            strCityName= parent.selectedItem.toString()
        }

        Log.e("selected value: ",""+ parent!!.selectedItem)

        //pos                                 get selected item position
        //view.getText()                      get lable of selected item
        //parent.getAdapter().getItem(pos)    get item by pos
        //parent.getAdapter().getCount()      get item count
        //parent.getCount()                   get item count
        //parent.getSelectedItem()            get selected item
        //and other...
    }


    fun onLoginClicked(user: User) {

        user.city=strCityName
        Log.e("register user details",""+user)

        if (user.username.equals("")) {
            errorUserName.setValue("Enter UserName")
        } else {
            errorUserName.setValue(null)
        }

        if (user.mobilenumber.equals("")) {
            erroMobileNumber.setValue("Please enter mobile number")
        } else if (isContactNoLengthTen(user.mobilenumber)) {
            erroMobileNumber.setValue("Please enter 10 digit mobile number")
        }else {
            erroMobileNumber.setValue(null)
        }

        if (user.address.equals("")) {
            errorUserAddress.setValue("Enter address")
        } else {
            errorUserAddress.setValue(null)
        }



        if (user.email.equals("")){
            errorUserEmail.setValue("Enter email-id.")
        }else if (isValidEmail(user.email)) {
            errorUserEmail.setValue("Enter correct email-id.")
        } else {
            errorUserEmail.setValue(null)
        }

        if (strCityName==null){
            Toast.makeText(getApplication(),"Please select city.",Toast.LENGTH_SHORT).show()
        }

        if (user.password.equals("")) {
            errorUserPassword.setValue("Enter password")
        } else {
            errorUserPassword.setValue(null)
        }

        if (!user.username.equals("") && !user.mobilenumber.equals("") &&
            !user.address.equals("") && !user.email.equals("") && !user.password.equals("")) {
            insertNewUser(user)
        }
    }

    fun insertNewUser(user: User) {
        disposable.add(
            repository.insertNewUser(user)
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableSingleObserver<Long>() {
                    override fun onSuccess(t: Long) {
                        Log.e("ViewModel Insert", t.toString())
                        status.postValue("1")
                    }
                    override fun onError(e: Throwable) {
                        Log.e("ViewModel error", e.message)
                        status.postValue(e.message)
                    }

                })!!
        )
    }



    fun isEmptyContactPersonField(): Boolean {
        return TextUtils.isEmpty(getContactPerson())
    }

    fun isContactNoLengthTen(mobilenumber: String?): Boolean {
        return getContactNumber(mobilenumber)!!.length < 10
    }

    fun isEmptyStreetField(): Boolean {
        return TextUtils.isEmpty(getStreetDetails())
    }

    fun getContactPerson(): String? {
        return if (userName == null) {
            ""
        } else userName.toString()
    }

    fun getContactNumber(mobilenumber: String?): String? {
        return if (mobilenumber.equals("")) {
            ""
        } else mobilenumber.toString()
    }

    fun getStreetDetails(): String? {
        return if (userAddress.toString() == "null") {
            ""
        } else userAddress.toString()
    }

    fun isFormValid(emailAddress: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()
    }


    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }





}