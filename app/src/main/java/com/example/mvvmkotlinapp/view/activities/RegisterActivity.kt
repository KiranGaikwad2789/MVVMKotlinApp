package com.example.mvvmkotlinapp.view.activities

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.ContentRegisterBinding
import com.example.mvvmkotlinapp.repository.room.User
import com.example.mvvmkotlinapp.utils.DeviceID
import com.example.mvvmkotlinapp.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity() : AppCompatActivity() {

    lateinit var registerModel: RegisterViewModel
    lateinit var binding: ContentRegisterBinding
    private var deviceID: DeviceID? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.content_register)
        binding.lifecycleOwner = this
        binding.registerViewModel=registerModel

        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);
        deviceID= DeviceID()

        var user = User(user_id = 0,username =binding.edtuserName.text.toString(), mobilenumber =binding.edtMobileNumber.text.toString(),
            address =binding.edtAddress.text.toString(), email =binding.edtEmail.text.toString(), password =binding.edtPassword.text.toString(),
            IMEI=deviceID!!.getIMEI(application),androidID=deviceID!!.getDeviceUniqueID(application))
        binding.user=user

        registerModel.status.observe(this, Observer { status ->
            status?.let {
                if(it=="1"){
                    Toast.makeText(this , "User registred Sucessfully" , Toast.LENGTH_LONG).show()
                    finish()
                } else
                    Toast.makeText(this , "Error occured: "+it , Toast.LENGTH_LONG).show()
                //Reset status value at first to prevent multitriggering
                //and to be available to trigger action again
                registerModel.status.value = null
            }
        })


        /*registerModel!!.getInsertResult()!!.observe(this,
            Observer<User?> { register ->
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

    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

}
