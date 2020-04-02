package com.example.mvvmkotlinapp.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.ActivityLoginBinding
import com.example.mvvmkotlinapp.model.LoginInfo
import com.example.mvvmkotlinapp.viewmodel.LoginViewModel
import java.util.*


class LoginActivity : AppCompatActivity() {

    private var loginViewModel: LoginViewModel? = null

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.setLifecycleOwner(this)
        binding.setLoginViewModel(loginViewModel)

        loginViewModel?.getUser()?.observe(this, Observer { loginUser->

            if (TextUtils.isEmpty(Objects.requireNonNull<Any>(loginUser).toString())) {
                    binding.txtEmailAddress.error = "Enter an E-Mail Address"
                    binding.txtEmailAddress.requestFocus()
                } else if (!loginUser!!.isEmailValid()) {
                    binding.txtEmailAddress.error = "Enter a Valid E-mail Address"
                    binding.txtEmailAddress.requestFocus()
                } else if (TextUtils.isEmpty(Objects.requireNonNull<Any>(loginUser).toString())) {
                    binding.txtPassword.error = "Enter a Password"
                    binding.txtPassword.requestFocus()
                } else if (!loginUser.isPasswordLengthGreaterThan5()) {
                    binding.txtPassword.error = "Enter at least 6 Digit password"
                    binding.txtPassword.requestFocus()
                } else {
                    binding.lblEmailAnswer.setText(loginUser.getStrEmailAddress())
                    binding.lblPasswordAnswer.setText(loginUser.getStrPassword())
                }


//            if(loginUser!=null){
//                Toasty.success(applicationContext, "Login Success", Toast.LENGTH_SHORT).show()
//                val mainIntent= Intent(this, MainActivity::class.java)
//                startActivity(mainIntent)
//                finish()
//            }else{
//                Toasty.error(applicationContext, "Login Failed, please try again", Toast.LENGTH_SHORT).show()
//            }
        })


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
}
