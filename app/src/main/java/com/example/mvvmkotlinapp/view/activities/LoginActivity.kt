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
import com.example.mvvmkotlinapp.repository.room.*
import com.example.mvvmkotlinapp.repository.room.tables.Product
import com.example.mvvmkotlinapp.repository.room.tables.ProductCategory
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
        insertCityOnly()
        setUpPermissions()

        loginViewModel!!.getUser.observe(this, Observer { status ->
            status?.let {
                if(it.success=="1"){

                    insertCityList()

                    userSession!!.setUserId(it.user_id.toString())
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

    private fun insertCityOnly() {

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
        //Insert data to tables
        loginViewModel?.deleteCityTable()
        loginViewModel?.insertCityListOnly(arrayListCity)
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
        val feature14 = Features(0,14, "Chat")

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
            arrayListFeaturesInfo!!.add(feature14)
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
        val r5 = Route(1,"All Route", "1",18.1890,73.122,1)
        val r1 = Route(2,"Kharadi Bypass", "1",18.1890,73.122,1)
        val r2 = Route(3,"Chandan Nagar road", "1",18.1890,73.122,1)
        val r3 = Route(4,"VimanNagar Chowk", "1",18.1890,73.122,1)
        val r4 = Route(5,"Wagholi", "1",18.1890,73.122,1)
        var arrayListRoute= ArrayList<Route>()


        if (arrayListRoute != null) {
            arrayListRoute!!.add(r5)
            arrayListRoute!!.add(r1)
            arrayListRoute!!.add(r2)
            arrayListRoute!!.add(r3)
            arrayListRoute!!.add(r4)
        }


        val o1 = Outlet(1,"Sai stores", 2,1,"1",18.1890,73.122)
        val o2 = Outlet(2,"OM shoppe", 2,1,"1",18.1890,73.122)
        val o3 = Outlet(3,"Jay Ganesh genaral shoppy", 1,2,"1",18.1890,73.122)
        val o4 = Outlet(4,"Balaji kirana shoppy", 3,2,"1",18.1890,73.122)
        val o5 = Outlet(5,"KK Shoppy", 3,2,"1",18.1890,73.122)
        val o6 = Outlet(6,"Shehal Shoppy", 4,3,"1",18.1890,73.122)
        val o7 = Outlet(7,"Shiksha Shoppy", 5,3,"1",18.1890,73.122)
        val o8 = Outlet(8,"KK Online market", 5,4,"1",18.1890,73.122)

        var arrayListOutlet= ArrayList<Outlet>()

        if (arrayListOutlet != null) {
            arrayListOutlet!!.add(o1)
            arrayListOutlet!!.add(o2)
            arrayListOutlet!!.add(o3)
            arrayListOutlet!!.add(o4)
            arrayListOutlet!!.add(o5)
            arrayListOutlet!!.add(o6)
            arrayListOutlet!!.add(o7)
            arrayListOutlet!!.add(o8)
        }

        val d1 = Distributor(1,"Sai stores D", 2,"1",18.1890,73.122)
        val d2 = Distributor(2,"OM shoppe D", 2,"1",18.1890,73.122)
        val d3 = Distributor(3,"Jay Ganesh genaral shoppy D", 2,"1",18.1890,73.122)
        val d4 = Distributor(4,"Balaji kirana shoppy D", 3,"1",18.1890,73.122)
        val d5 = Distributor(5,"KK Shoppy D", 3,"1",18.1890,73.122)

        var arrayListDistributor= ArrayList<Distributor>()

        if (arrayListDistributor != null) {
            arrayListDistributor!!.add(d1)
            arrayListDistributor!!.add(d2)
            arrayListDistributor!!.add(d3)
            arrayListDistributor!!.add(d4)
            arrayListDistributor!!.add(d5)
        }

        val ct1 = ProductCategory(1,"Benfeed", 1,1,1,"1")
        val ct2 = ProductCategory(2,"125 ml tube", 1,1,1,"1")
        val ct3 = ProductCategory(3,"500 ml tube", 2,1,1,"1")
        val ct4 = ProductCategory(4,"Double Dhamaka", 2,2,2,"1")
        val ct5 = ProductCategory(5,"Cones", 3,3,2,"1")
        val ct6 = ProductCategory(6,"Bar and Sticks", 3,2,2,"1")

        var arrayListProductCat= ArrayList<ProductCategory>()

        if (arrayListProductCat != null) {
            arrayListProductCat!!.add(ct1)
            arrayListProductCat!!.add(ct2)
            arrayListProductCat!!.add(ct3)
            arrayListProductCat!!.add(ct4)
            arrayListProductCat!!.add(ct5)
            arrayListProductCat!!.add(ct6)
        }


        val ctc1 = Product(1,"Pasta Sauce", 1,1,1,1,96.00,"Parle-G","1",false,0)
        val ctc2 = Product(2,"Salsa", 1,1,1,1,96.00,"Parle-G","1",false,0)
        val ctc3 = Product(3,"Facial tissue", 1,1,1,1,240.00,"Parle-G","1",false,0)

        val ctc4 = Product(4,"Peanut Buttor 125 ML", 2,1,1,1,224.00,"Parle-G","1",false,0)
        val ctc5 = Product(5,"Shrikhand 125Ml", 2,1,1,1,224.00,"Parle-G","1",false,0)


        val ctc6 = Product(6,"Lotion", 4,1,1,1,128.00,"Parle-G","1",false,0)
        val ctc7 = Product(7,"Baby wash", 4,1,1,1,128.00,"Parle-G","1",false,0)

        val ctc8 = Product(8,"mini cone buttor scotch", 5,1,1,1,144.00,"Parle-G","1",false,0)
        val ctc9 = Product(9,"Big cone black currant", 5,1,1,1,384.00,"Parle-G","1",false,0)

        var arrayListProduct= ArrayList<Product>()

        if (arrayListProduct != null) {
            arrayListProduct!!.add(ctc1)
            arrayListProduct!!.add(ctc2)
            arrayListProduct!!.add(ctc3)
            arrayListProduct!!.add(ctc4)
            arrayListProduct!!.add(ctc5)
            arrayListProduct!!.add(ctc6)
            arrayListProduct!!.add(ctc7)
            arrayListProduct!!.add(ctc8)
            arrayListProduct!!.add(ctc9)
        }


 //Insert data to tables
        loginViewModel?.deleteProductTable()
        loginViewModel?.deleteProductCatTable()
        loginViewModel?.deleteDistTable()
        loginViewModel?.deleteOutletTable()
        loginViewModel?.deleteRouteTable()
        loginViewModel?.deleteFeatureTable()
        loginViewModel?.deleteCityTable()

        loginViewModel?.insertCityList(arrayListCity,arrayListFeaturesInfo,arrayListRoute,arrayListOutlet,
            arrayListDistributor,arrayListProductCat,arrayListProduct)
    }

    private fun setUpPermissions() {

        val list = listOf<String>(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
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
