package com.example.mvvmkotlinapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.repository.room.Features

class HomePageAdapter (private val context: Context, private val features: List<Features>?) : RecyclerView.Adapter<HomePageAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, index: Int): ViewHolder {
        val rootView = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_features_menu, viewGroup, false)
        return ViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return features?.size!!
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, index: Int) {
        viewHolder.txtFeatureName.text = features?.get(index)?.featureName
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var txtFeatureName: TextView = itemView.findViewById(R.id.txtFeatureName) as TextView
    }
}