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
import com.example.mvvmkotlinapp.utils.CustomProgressDialog
import com.example.mvvmkotlinapp.utils.SkyggeProgressDialog
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

    private var reference1: Firebase? = null
    private var reference2: Firebase? = null
    private var currentDate: DateTime? =null
    private var userSession: UserSession? =null
    private val arryListMessageList: ArrayList<ChatMessage> = ArrayList()
    private var adapter: MessageAdapter? = null
    private val skyggeProgressDialog = CustomProgressDialog()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        firebaseChatViewModel = ViewModelProviders.of(this).get(FirebaseChatViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_chat, container, false)
        val view: View = binding.getRoot()
        binding.lifecycleOwner = this
        binding.firebaseChatViewModel=firebaseChatViewModel

        getObjectsInitialize()
        setMessageAdapter()

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


        activity?.let { skyggeProgressDialog.show(it,"Please Wait...") }
        reference1?.addChildEventListener(object : EventListener, ChildEventListener {
            override fun onCancelled(p0: FirebaseError?) {}

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {

                    val map = p0?.getValue(Map::class.java)

                    val chatId = map?.get("chatId").toString()
                    val messageId = map?.get("messageId").toString()
                    val senderId = map?.get("senderId").toString()
                    val receiverId = map?.get("receiverId").toString()
                    val message = map?.get("message").toString()
                    val timeStamp = map?.get("timeStamp").toString()
                    val messageType = map?.get("messageType").toString()

                    var chatMessage=ChatMessage(chatId,messageId,senderId,receiverId,message,timeStamp,messageType)
                    arryListMessageList.add(chatMessage)
                    adapter!!.notifyDataSetChanged()
                    skyggeProgressDialog?.dialog?.dismiss()
            }
            override fun onChildRemoved(p0: DataSnapshot?) {
            }
        })
        return view
    }

    private fun setMessageAdapter() {
        adapter = activity?.let { MessageAdapter(arryListMessageList, it) }
        binding.recyclerViewMessageList.adapter = adapter
        binding.recyclerViewMessageList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private fun getObjectsInitialize() {
        (getActivity() as HomePageActivity?)?.visibleMenuItems(14)
        currentDate= DateTime()
        userSession=UserSession(activity)
        Firebase.setAndroidContext(activity)
        reference1 = Firebase("https://chatapp-72cf4.firebaseio.com/messages/" + userSession?.getMobile().toString() + "_" + userSession!!.getChatWith())
        reference2 = Firebase("https://chatapp-72cf4.firebaseio.com/messages/" + userSession!!.getChatWith().toString() + "_" + userSession?.getMobile())

    }
}
