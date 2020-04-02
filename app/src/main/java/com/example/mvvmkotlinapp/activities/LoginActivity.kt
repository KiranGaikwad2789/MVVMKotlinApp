package com.example.mvvmkotlinapp.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.LoginFragmentBinding
import com.example.mvvmkotlinapp.viewmodel.LoginViewModel

import kotlinx.android.synthetic.main.activity_login.*

//This is Login luncher activity
class LoginActivity : AppCompatActivity() {

    lateinit var binding: LoginFragmentBinding

    var loginViewModel = LoginViewModel()
    //var count=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //binding.viewModel = loginViewModel
        //count=1
    }

}
