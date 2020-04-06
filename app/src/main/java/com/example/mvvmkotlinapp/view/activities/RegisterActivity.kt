package com.example.mvvmkotlinapp.view.activities

import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.ContentRegisterBinding
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.User
import com.example.mvvmkotlinapp.viewmodel.LoginViewModel

import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class RegisterActivity : AppCompatActivity() {

    private var loginViewModel: LoginViewModel? = null

    lateinit var binding: ContentRegisterBinding
    private lateinit var mDb:AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.content_register)
        binding.setLifecycleOwner(this)
        binding.setLoginViewModel(loginViewModel)
        loginViewModel!!.init()

        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);

        mDb = AppDatabase.getDatabase(applicationContext)

        binding.btnRegister.setOnClickListener {

            if (!binding.txtEmailAddress.text.toString().isEmpty() && !binding.txtPassword.text.toString().isEmpty()) {
                val user = User(0,binding.txtEmailAddress.text.toString(),binding.txtPassword.text.toString())

                doAsync {
                    // Put the student in database
                    mDb.userDao().insertAll(user)

                    uiThread {
                        toast("One record inserted.")
                    }
                }
                //InsertUser(this, chapterObj).execute()
            }

        }

    }

    private class InsertUser(var context: RegisterActivity, var user: User) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            context.mDb!!.userDao().insertAll(user)
            return true
        }
        override fun onPostExecute(bool: Boolean?) {
            if (bool!!) {
                Toast.makeText(context, "Added to Database", Toast.LENGTH_LONG).show()
            }
        }
    }

}
