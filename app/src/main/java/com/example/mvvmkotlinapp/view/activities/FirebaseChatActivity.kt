package com.example.mvvmkotlinapp.view.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.ActivityFirebaseChatBinding
import com.example.mvvmkotlinapp.viewmodel.FirebaseChatViewModel
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class FirebaseChatActivity : AppCompatActivity() {

    //https://chatapp-72cf4.firebaseio.com/

    lateinit var firebaseChatViewModel: FirebaseChatViewModel
    lateinit var binding: ActivityFirebaseChatBinding

    var al = ArrayList<String>()
    var totalUsers = 0
    var pd: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseChatViewModel = ViewModelProviders.of(this).get(FirebaseChatViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_firebase_chat)
        binding.lifecycleOwner = this
        binding.firebaseChatViewModel=firebaseChatViewModel

        setSupportActionBar(toolbar)

        pd = ProgressDialog(this@FirebaseChatActivity)
        pd!!.setMessage("Loading...")
        pd!!.show()

        val url = "https://chatapp-72cf4.firebaseio.com/users.json"

        val request =
            StringRequest(Request.Method.GET, url,
                Response.Listener { s -> doOnSuccess(s) },
                Response.ErrorListener { volleyError -> println("" + volleyError) })

        val rQueue = Volley.newRequestQueue(this@FirebaseChatActivity)
        rQueue.add(request)

        binding.usersList.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            UserSession.chatWith = al[position]
            startActivity(Intent(this@FirebaseChatActivity, ChatActivity::class.java))
        })

    }

    fun doOnSuccess(s: String?) {
        Log.e("User name: ",s.toString())
        try {
            val obj = JSONObject(s)
            val i: Iterator<*> = obj.keys()
            var key = ""
            Log.e("username : ",UserSession.username)
            while (i.hasNext()) {
                key = i.next().toString()
                if (!key.equals(UserSession.username)) {
                    Log.e("key : ",key.toString())
                    al.add(key)
                }
                totalUsers++
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (totalUsers <= 1) {
            binding.noUsersText.setVisibility(View.VISIBLE)
            binding.usersList.setVisibility(View.GONE)
        } else {
            binding.noUsersText.setVisibility(View.GONE)
            binding.usersList.setVisibility(View.VISIBLE)
            binding.usersList.setAdapter(
                ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    al
                )
            )
        }
        pd!!.dismiss()
    }


}
