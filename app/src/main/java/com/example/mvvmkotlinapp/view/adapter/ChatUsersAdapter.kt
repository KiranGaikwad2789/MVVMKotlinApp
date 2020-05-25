package com.example.mvvmkotlinapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.model.FirebaseUser
import com.example.mvvmkotlinapp.view.AESUtils
import java.util.*

class ChatUsersAdapter (ctx: Context, private var arrayListCity: List<FirebaseUser>?) :
    RecyclerView.Adapter<ChatUsersAdapter.MyViewHolder>() {

    private var aesEncryptions: AESUtils? =null

    private val inflater: LayoutInflater
    private val arraylist: ArrayList<FirebaseUser>
    init {
        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<FirebaseUser>()
        aesEncryptions= AESUtils()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = inflater.inflate(R.layout.row_chat_user_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtChatuserName.setText(aesEncryptions?.decrypt(arrayListCity!![position].username.toString()))
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