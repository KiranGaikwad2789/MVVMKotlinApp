package com.example.mvvmkotlinapp.view.activities

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.ManagePermissions
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.ActivityLoginBinding
import com.example.mvvmkotlinapp.model.LoginInfo
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.Features
import com.example.mvvmkotlinapp.repository.room.User
import com.example.mvvmkotlinapp.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread


class LoginActivity : AppCompatActivity() {

    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions


    private var loginViewModel: LoginViewModel? = null
    private var userSession: UserSession? = null

    lateinit var binding: ActivityLoginBinding
    private lateinit var mDb:AppDatabase

    private var arryListFeatures: ArrayList<Features>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.setLifecycleOwner(this)
        binding.setLoginViewModel(loginViewModel)
        loginViewModel!!.init()

        setSupportActionBar(toolbar)
        mDb = AppDatabase.getDatabase(applicationContext)
        userSession=UserSession(this)
        arryListFeatures= ArrayList<Features>()

        setUpPermissions()

        binding.btnLogin.setOnClickListener {

            if (!binding.txtEmailAddress.text.toString().isEmpty() && !binding.txtPassword.text.toString().isEmpty()) {
                val chapterObj = User(0,binding.txtEmailAddress.text.toString(),binding.txtPassword.text.toString(),"","","")
                //InsertUser(this, chapterObj).execute()

                doAsync {
                    // Get the student list from database
                    val list = mDb.userDao().findByName(binding.txtEmailAddress.text.toString()!!,binding.txtPassword.text.toString())

                    uiThread {
                       // toast("${list.size} records found.")
                        if (list.size==1){
                            userSession!!.setUserId(list.get(0).uid.toString())
                            //userSession!!.setUsername(list.get(0).email.toString())
                            userSession!!.setEmail(list.get(0).email.toString())
                            var intent=Intent(this@LoginActivity,HomePageActivity::class.java)
                            startActivity(intent)
                        }else if (list.size==0){
                            toast("User not registred.")
                        }
                    }
                }
            }

            //Login with Retrofit API
            //doLogin(binding.txtEmailAddress.text!!,binding.txtPassword.text)
        }

        binding.btnRegister.setOnClickListener {

            doAsync {
                // Get the student list from database
                val list = mDb.locationDao().getAllLocation();

                uiThread {
                    toast("${list.size} records found.")
                }
            }

            var intent=Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
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


    private fun doLogin(text: Editable, text1: Editable) {

        val username: String = text.toString()
        val password: String = text1.toString()

        loginViewModel!!.setUsername(text, text1)

        loginViewModel!!.doLogin()

        loginViewModel!!.getLoginData()!!.observe(this,
            Observer<LoginInfo?> { loginModel ->
                if (loginModel != null) {
                    if (loginModel.status.equals("1")) {
                        //userSession.setJWTToken(loginModel.getToken())
                        try {
                            //val json: String = JWTUtils.decoded(loginModel.getToken())
                            //val jsonObject = JSONObject(json)

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        //(context as InitialBaseActivity).commonMethodForActivity()
                    } else {
                       // Toast.makeText(context, loginModel.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
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
