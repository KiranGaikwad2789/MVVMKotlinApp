package com.example.mvvmkotlinapp.view.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.ContentRegisterBinding
import com.example.mvvmkotlinapp.repository.room.User
import com.example.mvvmkotlinapp.utils.DeviceID
import com.example.mvvmkotlinapp.viewmodel.RegisterViewModel
import com.firebase.client.Firebase
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject


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

        Firebase.setAndroidContext(this)

        var user = User(user_id = 0,username =binding.edtuserName.text.toString(), mobilenumber =binding.edtMobileNumber.text.toString(),
            address =binding.edtAddress.text.toString(), email =binding.edtEmail.text.toString(), password =binding.edtPassword.text.toString(),
            IMEI=deviceID!!.getIMEI(application),androidID=deviceID!!.getDeviceUniqueID(application))
        binding.user=user

        registerModel.status.observe(this, Observer { status ->
            status?.let {
                if(it=="1"){

                    val pd = ProgressDialog(this@RegisterActivity)
                    pd.setMessage("Loading...")
                    pd.show()

                    //https://chatapp-72cf4.firebaseio.com/
                    val url = "https://chatapp-72cf4.firebaseio.com/users.json"

                    val request =
                        StringRequest(Request.Method.GET, url,
                            Response.Listener { s ->
                                val reference =
                                    Firebase("https://chatapp-72cf4.firebaseio.com/users")
                                if (s == "null") {
                                    reference.child(binding.edtMobileNumber.text.toString()).child("password").setValue(binding.edtPassword.text.toString())
                                    Toast.makeText(this@RegisterActivity, "registration successful", Toast.LENGTH_LONG).show()
                                    finish()
                                } else {
                                    try {
                                        val obj = JSONObject(s)
                                        if (!obj.has(binding.edtMobileNumber.text.toString())) {
                                            reference.child(binding.edtMobileNumber.text.toString()).child("password").setValue(binding.edtPassword.text.toString())
                                            Toast.makeText(
                                                this@RegisterActivity,
                                                "registration successful",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this@RegisterActivity,
                                                "username already exists",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            finish()
                                        }
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }
                                pd.dismiss()
                            }, Response.ErrorListener { volleyError ->
                                println("" + volleyError)
                                pd.dismiss()
                            })

                    val rQueue = Volley.newRequestQueue(this@RegisterActivity)
                    rQueue.add(request)


                    //Toast.makeText(this , "User registred Sucessfully" , Toast.LENGTH_LONG).show()
                    //finish()
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
