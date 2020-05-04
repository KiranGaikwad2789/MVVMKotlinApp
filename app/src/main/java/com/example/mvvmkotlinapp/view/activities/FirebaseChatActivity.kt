package com.example.mvvmkotlinapp.view.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvmkotlinapp.R

import kotlinx.android.synthetic.main.activity_firebase_chat.*

class FirebaseChatActivity : AppCompatActivity() {

    //https://chatapp-72cf4.firebaseio.com/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_chat)
        setSupportActionBar(toolbar)

    }

}
