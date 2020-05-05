package com.example.mvvmkotlinapp.view.activities

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.DateTime
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.ActivityChatBinding
import com.example.mvvmkotlinapp.viewmodel.FirebaseChatViewModel
import com.firebase.client.ChildEventListener
import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class ChatActivity : AppCompatActivity() {

    lateinit var firebaseChatViewModel: FirebaseChatViewModel
    lateinit var binding: ActivityChatBinding

    var reference1: Firebase? = null
    var reference2: Firebase? = null
    private var currentDate: DateTime? =null
    private var userSession: UserSession? =null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseChatViewModel = ViewModelProviders.of(this).get(FirebaseChatViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        binding.lifecycleOwner = this
        binding.firebaseChatViewModel=firebaseChatViewModel

        setSupportActionBar(toolbar)
        currentDate= DateTime()
        userSession=UserSession(this)

        Firebase.setAndroidContext(this)
        reference1 = Firebase("https://chatapp-72cf4.firebaseio.com/messages/" + UserSession.username.toString() + "_" + UserSession.chatWith)
        reference2 = Firebase("https://chatapp-72cf4.firebaseio.com/messages/" + UserSession.chatWith.toString() + "_" + UserSession.username)

        binding.sendButton.setOnClickListener(View.OnClickListener {
            val messageText: String = binding.messageArea.getText().toString()
            if (messageText != "") {
                val map: MutableMap<String, String> = HashMap()

                //map["message"] = messageText
                //map["user"] = UserSession.username

                map["chatId"] =  UserSession.username + "_" + UserSession.chatWith
                map["messageId"] = userSession?.getUserId() +""+ currentDate!!.orderDateFormater()
                map["senderId"] = UserSession.username
                map["receiverId"] = UserSession.chatWith
                map["message"] = messageText
                map["timeStamp"] = currentDate!!.getDateFormater()
                map["messageType"] = "TEXT"
                reference1!!.push().setValue(map)
                reference2!!.push().setValue(map)
            }
        })


        reference1?.addChildEventListener(object :EventListener, ChildEventListener {
            override fun onCancelled(p0: FirebaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {

                Log.e("MAP S value: ",""+p1)

                if(!TextUtils.isEmpty(p1)){
                    val map = p0?.getValue(Map::class.java)

                    val chatId = map?.get("chatId").toString()
                    val messageId = map?.get("messageId").toString()
                    val senderId = map?.get("senderId").toString()
                    val receiverId = map?.get("receiverId").toString()
                    val message = map?.get("message").toString()
                    val timeStamp = map?.get("timeStamp").toString()
                    val messageType = map?.get("messageType").toString()

                    if (senderId == UserSession.username) {
                        addMessageBox("You:-\n$message", 1)
                    } else {
                        addMessageBox(UserSession.chatWith.toString() + ":-\n" + message, 2)
                    }
                }
            }
            override fun onChildRemoved(p0: DataSnapshot?) {
            }

        }
        )


    }

    fun addMessageBox(message: String?, type: Int) {
        val textView = TextView(this)
        textView.text = message
        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(0, 0, 0, 10)
        textView.layoutParams = lp
        if (type == 1) {
            textView.setBackgroundResource(R.drawable.rounded_corner1)
        } else {
            textView.setBackgroundResource(R.drawable.rounded_corner2)
        }
        binding.layout1.addView(textView)
        binding.scrollView.fullScroll(View.FOCUS_DOWN)
    }
}
