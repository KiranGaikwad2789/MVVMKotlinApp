package com.example.mvvmkotlinapp.common

import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

@BindingMethods(value = [BindingMethod(type = RecyclerViewBindingAdapters::class,
    attribute = "app:OnItemClickListener", method = "setOnItemClickListener")])
object RecyclerViewBindingAdapters {

}