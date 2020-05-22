package com.example.mvvmkotlinapp.view.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Base64
import android.util.Log
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
import com.example.mvvmkotlinapp.model.FirebaseUser
import com.example.mvvmkotlinapp.repository.room.User
import com.example.mvvmkotlinapp.utils.DeviceID
import com.example.mvvmkotlinapp.view.AESUtils
import com.example.mvvmkotlinapp.viewmodel.RegisterViewModel
import com.firebase.client.Firebase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject
import java.security.Security
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


class RegisterActivity() : AppCompatActivity() {

    lateinit var registerModel: RegisterViewModel
    lateinit var binding: ContentRegisterBinding
    private var deviceID: DeviceID? =null
    private var aesEncryptions: AESUtils? =null


    @SuppressLint("StringFormatInvalid")
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
        aesEncryptions= AESUtils()

        Firebase.setAndroidContext(this)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("TAG", "getInstanceId failed ", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
                Log.e("getInstanceId ", token)
             })

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
                                val reference = Firebase("https://chatapp-72cf4.firebaseio.com/users")
                                Log.e("Response1 ",""+s)
                                if (s == "null") {
                                    var user_id= aesEncryptions!!.encrypt("user_id")
                                    reference.child(deviceID!!.getIMEI(application).toString()).setValue(
                                        FirebaseUser(user_id = aesEncryptions!!.encrypt(it.toString()),username = aesEncryptions!!.encrypt(binding.edtuserName.text.toString()), mobilenumber = aesEncryptions!!.encrypt(binding.edtMobileNumber.text.toString()))
                                    )

                                    //reference.child(deviceID!!.getIMEI(application)).setValue(User(user_id=it.toInt(),username = binding.edtuserName.text.toString(), mobilenumber = binding.edtMobileNumber.text.toString(), address = null, email = null, password = null, city=null,IMEI=null,androidID=null))

                                    //reference.child(deviceID!!.getIMEI(application)).child("username").setValue(binding.edtuserName.text.toString())

                                    Toast.makeText(this@RegisterActivity, "registration successful", Toast.LENGTH_LONG).show()
                                    finish()
                                } else {
                                    try {
                                        val obj = JSONObject(s)
                                        Log.e("Response2 ",""+obj)
                                        if (!obj.has(deviceID!!.getIMEI(application))) {

                                            reference.child(deviceID!!.getIMEI(application).toString()).setValue(
                                                FirebaseUser(user_id = aesEncryptions!!.encrypt(it.toString()),username = aesEncryptions!!.encrypt(binding.edtuserName.text.toString()), mobilenumber = aesEncryptions!!.encrypt(binding.edtMobileNumber.text.toString()))
                                            )

                                            //reference.child(deviceID!!.getIMEI(application).toString()).setValue(User(user_id=it.toInt(),username = aesEncryptions!!.encrypt(binding.edtuserName.text.toString()), mobilenumber = aesEncryptions!!.encrypt(binding.edtMobileNumber.text.toString()), address = null, email = null, password = null, city=null,IMEI=null,androidID=null))

                                            //reference.child(deviceID!!.getIMEI(application)).setValue(User(user_id=it.toInt(),username = binding.edtuserName.text.toString(), mobilenumber = binding.edtMobileNumber.text.toString(), address = null, email = null, password = null, city=null,IMEI=null,androidID=null))

                                            //reference.child(deviceID!!.getIMEI(application)).child("username").setValue(binding.edtuserName.text.toString())

                                            Toast.makeText(this@RegisterActivity, "registration successful", Toast.LENGTH_LONG).show()
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
