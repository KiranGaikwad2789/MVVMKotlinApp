package com.example.mvvmkotlinapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlinapp.BR
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.RowFeaturesMenuBinding
import com.example.mvvmkotlinapp.repository.room.Features
import com.example.mvvmkotlinapp.view.adapter.HomePageAdapter.ViewHolder


class HomePageAdapter (private val context: Context, private val features: List<Features>?) : RecyclerView.Adapter<ViewHolder>(){


    // Inflating Layout and ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowFeaturesMenuBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_features_menu, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return features?.size!!
    }

    // Bind data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(features!!.get(position))
    }

    // Creating ViewHolder
    class ViewHolder(val binding: RowFeaturesMenuBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Features) {
            binding.setVariable(BR.features, data) //BR - generated class; BR.user - 'user' is variable name declared in layout
            binding.executePendingBindings()
        }
    }
}