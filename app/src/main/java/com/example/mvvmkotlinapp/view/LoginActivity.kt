package com.example.mvvmkotlinapp.view

import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.ActivityLoginBinding
import com.example.mvvmkotlinapp.model.LoginInfo
import com.example.mvvmkotlinapp.viewmodel.LoginViewModel
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    private var loginViewModel: LoginViewModel? = null

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.setLifecycleOwner(this)
        binding.setLoginViewModel(loginViewModel)
        loginViewModel!!.init()

        binding.btnLogin.setOnClickListener {

            fun onFormSubmit() {
                if (loginViewModel!!.isFormValid()) {
                    doLogin(binding.txtEmailAddress.text!!,binding.txtPassword.text)
                }
            }


        }

//        loginViewModel!!.getUser()!!.observe(this, androidx.lifecycle.Observer() {
//            fun onChanged(@Nullable loginUser: LoginInfo) {
//                if (TextUtils.isEmpty(Objects.requireNonNull<Any>(loginUser).toString())) {
//                    binding.txtEmailAddress.error = "Enter an E-Mail Address"
//                    binding.txtEmailAddress.requestFocus()
//                } else if (!loginUser.isEmailValid()) {
//                    binding.txtEmailAddress.error = "Enter a Valid E-mail Address"
//                    binding.txtEmailAddress.requestFocus()
//                } else if (TextUtils.isEmpty(Objects.requireNonNull<Any>(loginUser).toString())) {
//                    binding.txtPassword.error = "Enter a Password"
//                    binding.txtPassword.requestFocus()
//                } else if (!loginUser.isPasswordLengthGreaterThan5()) {
//                    binding.txtPassword.error = "Enter at least 6 Digit password"
//                    binding.txtPassword.requestFocus()
//                } else {
//                    binding.lblEmailAnswer.setText(loginUser.getStrEmailAddress())
//                    binding.lblPasswordAnswer.setText(loginUser.getStrPassword())
//                }
//            }
//        })
    }


    private fun doLogin(text: Editable, text1: Editable) {
        val username: String = text.toString()
        val password: String = text1.toString()
        loginViewModel!!.setUsername(username, password)
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
}
