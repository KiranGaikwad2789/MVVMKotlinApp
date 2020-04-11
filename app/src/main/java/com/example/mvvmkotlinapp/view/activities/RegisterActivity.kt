package com.example.mvvmkotlinapp.view.activities

import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.ContentRegisterBinding
import com.example.mvvmkotlinapp.model.RegisterUserModel
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.User
import com.example.mvvmkotlinapp.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class RegisterActivity : AppCompatActivity() {

    private var registerModel: RegisterViewModel? = null
    lateinit var binding: ContentRegisterBinding
    private lateinit var mDb:AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.content_register)
        binding.lifecycleOwner = this
        binding.registerViewModel=registerModel

        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);

        mDb = AppDatabase.getDatabase(applicationContext)
        registerModel!!.registerViewModel(this)

        /*registerModel!!.getUserDetails()!!.observe(this,
            Observer<RegisterUserModel?> { register ->
                if (register != null) {

                    if (register.equals("1")) {
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
            })*/



        binding.btnRegisterUser.setOnClickListener {

            if (!binding.edtuserName.text.toString().isEmpty() && !binding.edtMobileNumber.text.toString().isEmpty()) {
                val user = User(0,binding.edtuserName.text.toString(),binding.edtMobileNumber.text.toString(),binding.edtAddress.text.toString(),
                    binding.edtEmail.text.toString(),binding.edtPassword.text.toString())

                doAsync {
                    // Put the student in database
                    mDb.userDao().insertAll(user)

                    uiThread {
                        toast("One record inserted.")
                        finish()
                    }
                }
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

}
