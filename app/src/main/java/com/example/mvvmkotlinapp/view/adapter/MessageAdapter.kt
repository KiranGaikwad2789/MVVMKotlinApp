package com.example.mvvmkotlinapp.view.adapter

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
import com.squareup.picasso.Picasso
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
        holder.bindForecast(messages[position],userSession,currentDate,context)
    }

    override fun getItemCount() = messages.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindForecast(
            message: ChatMessage,
            userSession: UserSession?,
            currentDate: DateTime?,
            context: Context
        ) {

            if (message.messageType.equals("TEXT")) {
                if (message.senderId.equals(userSession?.getIMEI())) {
                    itemView.layoutSenderID.visibility=View.VISIBLE
                    itemView.txtMessage.text = message.message
                    itemView.txtSenderMessageTime.text = currentDate?.chatDateForamt(message.timeStamp!!).toString()
                } else {
                    itemView.layoutReceiverID.visibility=View.VISIBLE
                    itemView.txtMessageReciver.text = message.message
                    itemView.txtMessageReciverTime.text = currentDate?.chatDateForamt(message.timeStamp!!).toString()
                }
            } else  if (message.messageType.equals("IMAGE")) {
                if (message.senderId.equals(userSession?.getIMEI())) {
                    itemView.imgSenderImage.visibility=View.VISIBLE
                    Picasso.with(context).load(message.message).into(itemView.imgSenderImage)
                } else {
                    itemView.imgReceiverImage.visibility=View.VISIBLE
                    Picasso.with(context).load(message.message)
                        .into(itemView.imgReceiverImage)
                }
            }else if (message.messageType.equals("PDF")) {
                if (message.senderId.equals(userSession?.getIMEI())) {
                    itemView.layoutSenderID.visibility=View.VISIBLE
                    itemView.txtMessage.text = message.fileName
                    itemView.txtSenderMessageTime.text = currentDate?.chatDateForamt(message.timeStamp!!).toString()
                } else {
                    itemView.layoutReceiverID.visibility=View.VISIBLE
                    itemView.txtMessageReciver.text = message.fileName
                    itemView.txtMessageReciverTime.text = currentDate?.chatDateForamt(message.timeStamp!!).toString()
                }
            }
        }
    }
}