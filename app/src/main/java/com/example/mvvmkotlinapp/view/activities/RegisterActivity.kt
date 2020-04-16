package com.example.mvvmkotlinapp.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.ContentRegisterBinding
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.User
import com.example.mvvmkotlinapp.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import kotlin.coroutines.CoroutineContext


class RegisterActivity() : AppCompatActivity() , CoroutineScope {
//imp
    //https://stackoverflow.com/questions/44364240/android-room-get-the-id-of-new-inserted-row-with-auto-generate
//                android:onClick="@{() -> registerViewModel.onLoginClicked()}"

    //https://www.freecodecamp.org/news/how-to-simplify-android-app-architecture/
    //https://itnext.io/room-persistence-library-using-mutablelivedata-observable-to-update-the-ui-after-a-database-6836d25e8267?gi=88cf12ebce2d

    lateinit var registerModel: RegisterViewModel
    lateinit var binding: ContentRegisterBinding
    private lateinit var mDb:AppDatabase

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


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


        binding.btnRegisterUser.setOnClickListener {

            if (!binding.edtuserName.text.toString().isEmpty() && !binding.edtMobileNumber.text.toString().isEmpty()) {
                val user = User(0,binding.edtuserName.text.toString(),binding.edtMobileNumber.text.toString(),binding.edtAddress.text.toString(),binding.edtEmail.text.toString(),binding.edtPassword.text.toString())

                doAsync {
                    // Put the student in database
                    mDb.userDao().insertAll(user)

                    uiThread {
                        toast("User successfully saved.")
                        finish()
                    }
                }
                //InsertUser(this, chapterObj).execute()
            }

        }



        /*registerModel!!.getInsertResult()!!.observe(this,
            Observer<Long> { result ->
                if (result.equals(1)) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "User successfully saved",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "Error saving person", Toast.LENGTH_SHORT)
                        .show()
                }
            })*/



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
