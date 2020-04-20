package com.example.mvvmkotlinapp.view.activities

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.ManagePermissions
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.ActivityLoginBinding
import com.example.mvvmkotlinapp.repository.room.City
import com.example.mvvmkotlinapp.repository.room.Features
import com.example.mvvmkotlinapp.repository.room.User
import com.example.mvvmkotlinapp.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast


class LoginActivity : AppCompatActivity() {

    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions

    private var loginViewModel: LoginViewModel? = null
    lateinit var bindingLogin: ActivityLoginBinding
    private var userSession: UserSession? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeBindingView()
        initializeObjects()
        insertCityList()
        setUpPermissions()

        loginViewModel!!.getUser.observe(this, Observer { status ->
            status?.let {
                if(it.success=="1"){

                    userSession!!.setUserId(it.uid.toString())
                    userSession!!.setUsername(it.username.toString())
                    userSession!!.setEmail(it.email.toString())
                    userSession!!.setMobile(it.mobilenumber.toString())
                    userSession!!.setCurrentCity(it.city.toString())

                    var intent=Intent(this@LoginActivity,HomePageActivity::class.java)
                    startActivity(intent)
                    finish()
                } else{
                    Toast.makeText(this , "Error occured: "+it , Toast.LENGTH_LONG).show()
                    loginViewModel!!.getUser.value = null
                }
            }
        })
    }

    private fun initializeObjects() {
        setSupportActionBar(toolbar)
        userSession=UserSession(this)
    }

    private fun initializeBindingView() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        bindingLogin = DataBindingUtil.setContentView(this, R.layout.activity_login)
        bindingLogin.lifecycleOwner=this
        bindingLogin.loginViewModel=loginViewModel

        var user = User(username ="", mobilenumber =bindingLogin.edtMobileNumber.text.toString(), address ="", email ="", password =bindingLogin.edtPassword.text.toString())
        bindingLogin.user=user
    }

    private fun insertCityList() {

        val feature1 = Features(0,1, "Capture Outlet")
        val feature2 = Features(0,2, "New Lead")
        val feature3 = Features(0,3, "Customer Profile")
        val feature4 = Features(0,4, "New order")
        val feature5 = Features(0,5, "Order Delivery")
        val feature6 = Features(0,6, "My Dashboard")
        val feature7 = Features(0,7, "Weekly Stock")
        val feature8 = Features(0,8, "Product menu")
        val feature9 = Features(0,9, "My DSR")
        val feature10 = Features(0,10, "My TimeSheet")
        val feature11 = Features(0,11, "My Claims")
        val feature12 = Features(0,12, "Manage Leave")
        val feature13 = Features(0,13, "Customer Support")

        var arrayListFeaturesInfo= ArrayList<Features>()

        if (arrayListFeaturesInfo != null) {
            arrayListFeaturesInfo!!.add(feature1)
            arrayListFeaturesInfo!!.add(feature2)
            arrayListFeaturesInfo!!.add(feature3)
            arrayListFeaturesInfo!!.add(feature4)
            arrayListFeaturesInfo!!.add(feature5)
            arrayListFeaturesInfo!!.add(feature6)
            arrayListFeaturesInfo!!.add(feature7)
            arrayListFeaturesInfo!!.add(feature8)
            arrayListFeaturesInfo!!.add(feature9)
            arrayListFeaturesInfo!!.add(feature10)
            arrayListFeaturesInfo!!.add(feature11)
            arrayListFeaturesInfo!!.add(feature12)
            arrayListFeaturesInfo!!.add(feature13)
        }


        val c1 = City(0,1, "Pune")
        val c2 = City(0,2, "Mumbai")
        val c3 = City(0,3, "Nashik")
        val c4 = City(0,4, "Dhule")
        val c5 = City(0,5, "Nandurbar")
        val c6 = City(0,6, "Nagpur")
        val c7 = City(0,7, "Jalgaon")
        var arrayListCity= ArrayList<City>()

        if (arrayListCity != null) {
            arrayListCity!!.add(c1)
            arrayListCity!!.add(c2)
            arrayListCity!!.add(c3)
            arrayListCity!!.add(c4)
            arrayListCity!!.add(c5)
            arrayListCity!!.add(c6)
            arrayListCity!!.add(c7)
        }

        loginViewModel?.deleteFeatureTable()
        loginViewModel?.deleteCityTable()
        loginViewModel?.insertCityList(arrayListCity,arrayListFeaturesInfo)
    }

    private fun setUpPermissions() {

        val list = listOf<String>(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        // Initialize a new instance of ManagePermissions class
        managePermissions = ManagePermissions(this,list,PermissionsRequestCode)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()

    }


    // Receive the permissions request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PermissionsRequestCode ->{
                val isPermissionsGranted = managePermissions
                    .processPermissionsResult(requestCode,permissions,grantResults)

                if(isPermissionsGranted){
                    // Do the task now
                    toast("Permissions granted.")
                }else{
                    toast("Permissions denied.")
                }
                return
            }
        }
    }
}
