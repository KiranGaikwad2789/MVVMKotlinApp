package com.example.mvvmkotlinapp.view.fragmets

import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.DateTime
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.FragmentChatBinding
import com.example.mvvmkotlinapp.model.ChatMessage
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.adapter.MessageAdapter
import com.example.mvvmkotlinapp.viewmodel.FirebaseChatViewModel
import com.firebase.client.ChildEventListener
import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment() {

    lateinit var firebaseChatViewModel: FirebaseChatViewModel
    lateinit var binding: FragmentChatBinding

    var reference1: Firebase? = null
    var reference2: Firebase? = null
    var databaseReference: DatabaseReference? = null
    private var currentDate: DateTime? =null
    private var userSession: UserSession? =null
    val toReturn: ArrayList<ChatMessage> = ArrayList()
    private var adapter: MessageAdapter? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        firebaseChatViewModel = ViewModelProviders.of(this).get(FirebaseChatViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_chat, container, false)
        val view: View = binding.getRoot()
        binding.lifecycleOwner = this
        binding.firebaseChatViewModel=firebaseChatViewModel
        (getActivity() as HomePageActivity?)?.visibleMenuItems(14)

        currentDate= DateTime()
        userSession=UserSession(activity)

        //get reference to our db
        databaseReference = FirebaseDatabase.getInstance().reference

        adapter = activity?.let { MessageAdapter(toReturn, it) }
        binding.recyclerViewMessageList.adapter = adapter
        binding.recyclerViewMessageList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        Firebase.setAndroidContext(activity)
        reference1 = Firebase("https://chatapp-72cf4.firebaseio.com/messages/" + userSession?.getMobile().toString() + "_" + userSession!!.getChatWith())
        reference2 = Firebase("https://chatapp-72cf4.firebaseio.com/messages/" + userSession!!.getChatWith().toString() + "_" + userSession?.getMobile())

        binding.imgBtnSendMessage.setOnClickListener(View.OnClickListener {

            val messageText: String = binding.edtMessage.getText().toString()

            if (messageText != "") {

                val map: MutableMap<String, String> = HashMap()

                map["chatId"] =  userSession?.getMobile() + "_" + userSession!!.getChatWith()
                map["messageId"] = userSession?.getUserId() +""+ currentDate!!.orderDateFormater()
                map["senderId"] = userSession?.getMobile().toString()
                map["receiverId"] = userSession!!.getChatWith().toString()
                map["message"] = messageText
                map["timeStamp"] = currentDate!!.getDateTime()
                map["messageType"] = "TEXT"
                reference1!!.push().setValue(map)
                reference2!!.push().setValue(map)
                binding.edtMessage.setText("")
                binding.edtMessage.setInputType(InputType.TYPE_NULL)
            }
        })


       /* val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                Log.e("dataSnapshot value ",""+dataSnapshot)

                val toReturn: ArrayList<ChatMessage> = ArrayList();

                for(data in dataSnapshot.children){

                    val messageData = data.getValue<ChatMessage>(ChatMessage::class.java)
                    Log.e("dataSnapshot children ",""+dataSnapshot.children)

                    //unwrap
                    val message = messageData?.let { it } ?: continue

                    toReturn.add(message)
                }
                Log.e("toReturn value ",""+toReturn.toString())
                setupAdapter(toReturn)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //log error
            }
        }
        databaseReference?.child("messages")?.addValueEventListener(postListener)*/


        reference1?.addChildEventListener(object : EventListener, ChildEventListener {
            override fun onCancelled(p0: FirebaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {

                //if(!TextUtils.isEmpty(p1)){
                    val map = p0?.getValue(Map::class.java)

                    val chatId = map?.get("chatId").toString()
                    val messageId = map?.get("messageId").toString()
                    val senderId = map?.get("senderId").toString()
                    val receiverId = map?.get("receiverId").toString()
                    val message = map?.get("message").toString()
                    val timeStamp = map?.get("timeStamp").toString()
                    val messageType = map?.get("messageType").toString()

                    var chatMessage=ChatMessage(chatId,messageId,senderId,receiverId,message,timeStamp,messageType)
                    toReturn.add(chatMessage)
                    adapter!!.notifyDataSetChanged()

                    Log.e("toReturn data ",""+toReturn.toString())

                    //setupAdapter(toReturn)
                //}
            }
            override fun onChildRemoved(p0: DataSnapshot?) {
            }

        }
        )

        // Inflate the layout for this fragment
        return view
    }

    private fun setupAdapter(arryListMessage: ArrayList<ChatMessage>) {
        Log.e("toReturn size ",""+arryListMessage.size)
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.recyclerViewMessageList.layoutManager = linearLayoutManager
        binding.recyclerViewMessageList.adapter = activity?.let {
            MessageAdapter(arryListMessage,
                it
            )
        }

        //scroll to bottom
        binding.recyclerViewMessageList.scrollToPosition(arryListMessage.size - 1)

    }

   /* fun addMessageBox(message: String?, type: Int) {
        val textView = TextView(activity)
        textView.text = message

        *//*val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            weight = 1.0f
            gravity = Gravity.TOP
        }*//*

        val rlp = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        rlp.setMargins(0, 0, 0, 10)
        textView.layoutParams = rlp

        if (type == 1) {

            textView.setBackgroundResource(R.drawable.rounded_corner1)
            textView.gravity=Gravity.LEFT
            rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        } else {

            textView.setBackgroundResource(R.drawable.rounded_corner2)
            textView.gravity=Gravity.RIGHT
            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)

        }
        binding.layout1.addView(textView)
        binding.scrollView.fullScroll(View.FOCUS_DOWN)
    }*/
}
