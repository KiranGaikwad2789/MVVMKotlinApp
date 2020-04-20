package com.example.mvvmkotlinapp.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.UserSession

import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {


    private val AUTO_HIDE_DELAY_MILLIS = 3000
    private val mHideHandler = Handler()

    private var userSession: UserSession? = null
    var view: View? = null
    var isFirstTime = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        userSession= UserSession(this)

        mHideHandler.postDelayed(Runnable {
            if (userSession != null) {
                if (userSession!!.getUserId() != null) {
                    var intent= Intent(this, HomePageActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    var intent= Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, AUTO_HIDE_DELAY_MILLIS.toLong())
    }

}
