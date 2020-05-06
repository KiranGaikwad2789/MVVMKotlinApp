package com.example.mvvmkotlinapp.view.adapter

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.DateTime
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.model.ChatMessage
import kotlinx.android.synthetic.main.row_message_item.view.*

class MessageAdapter (val messages: ArrayList<ChatMessage>,var context: Context) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private var userSession: UserSession? =null
    private var currentDate: DateTime? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        userSession=UserSession(context)
        currentDate= DateTime()
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_message_item, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(messages[position],userSession,currentDate)


    }

    override fun getItemCount() = messages.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindForecast(message: ChatMessage, userSession: UserSession?, currentDate: DateTime?) {
            if (message.senderId == userSession?.getMobile()) {
                itemView.layoutReceiverID.visibility=View.GONE
                itemView.txtMessage.text = message.message
                itemView.txtSenderMessageTime.text = currentDate?.chatDateForamt(message.timeStamp!!).toString()
            } else {
                itemView.layoutSenderID.visibility=View.GONE
                itemView.txtMessageReciver.text = message.message
                itemView.txtMessageReciverTime.text = currentDate?.chatDateForamt(message.timeStamp!!).toString()
            }
            //itemView.txtMessage.text = message.message
        }
    }
}