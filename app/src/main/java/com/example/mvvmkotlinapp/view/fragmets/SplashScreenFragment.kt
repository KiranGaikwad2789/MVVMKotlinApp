package com.example.mvvmkotlinapp.view.fragmets

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.activities.LoginActivity

/**
 * A simple [Fragment] subclass.
 */
class SplashScreenFragment : Fragment() {

    private val AUTO_HIDE_DELAY_MILLIS = 3000
    private val mHideHandler = Handler()

    private var userSession: UserSession? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_splash_screen, container, false)
 
        userSession= UserSession(activity)

        mHideHandler.postDelayed(Runnable {
            if (userSession != null) {
                if (userSession!!.getUserId() != null) {
                    val intent = Intent(activity, HomePageActivity::class.java)
                    intent.putExtra("from", "HomePageFragment")
                    startActivity(intent)
                } else {
                    var intent= Intent(activity, LoginActivity::class.java)
                    intent.putExtra("from", "")
                    startActivity(intent)
                }
            }
        }, AUTO_HIDE_DELAY_MILLIS.toLong())

        return view
    }

}
