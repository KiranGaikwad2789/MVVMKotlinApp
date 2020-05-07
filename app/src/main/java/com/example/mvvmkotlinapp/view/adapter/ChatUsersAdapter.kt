package com.example.mvvmkotlinapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlinapp.R
import java.util.*

class ChatUsersAdapter (ctx: Context, private var arrayListCity: List<String>?) :
    RecyclerView.Adapter<ChatUsersAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater
    private val arraylist: ArrayList<String>
    init {
        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<String>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatUsersAdapter.MyViewHolder {
        val view = inflater.inflate(R.layout.row_chat_user_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatUsersAdapter.MyViewHolder, position: Int) {
        holder.txtChatuserName.setText(this!!.arrayListCity!![position]?.toString())
    }

    override fun getItemCount(): Int {
        return arrayListCity!!.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtChatuserName: TextView
        init {
            txtChatuserName = itemView.findViewById(R.id.txtChatuserName) as TextView
        }

    }
}