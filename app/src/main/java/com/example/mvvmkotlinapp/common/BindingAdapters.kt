package com.example.mvvmkotlinapp.common

import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


@BindingMethods(value = [BindingMethod(type = BindingAdapters::class, attribute = "app:onNavigationItemSelected", method = "setOnNavigationItemSelectedListener")])
object BindingAdapters {

    @BindingAdapter("onNavigationItemSelected")
    @JvmStatic
    fun setOnNavigationItemSelectedListener(
        view: BottomNavigationView, listener: BottomNavigationView.OnNavigationItemSelectedListener?
        //view1: NavigationView, listener1: NavigationView.OnNavigationItemSelectedListener?

    ) {
        view.setOnNavigationItemSelectedListener(listener)
        //view1.setNavigationItemSelectedListener(listener1)

    }

    /*@BindingAdapter("onNavigationItemSelected1")
    @JvmStatic
    fun setOnNavigationItemSelectedListener1(
        view: NavigationView, listener: NavigationView.OnNavigationItemSelectedListener?

    ) {
        view.setOnNavigationItemReselectedListener(listener)
    }*/

    @BindingAdapter("selectedItemPosition")
    fun setSelectedItemPosition(
        view: BottomNavigationView, position: Int
    ) {
        view.selectedItemId = position
    }


    /*@BindingAdapter("onNavigationItemSelected")
    fun setOnNavigationItemSelectedListener(
        view: BottomNavigationView, listener: BottomNavigationView.OnNavigationItemSelectedListener?
    ) {
        view.setOnNavigationItemSelectedListener(listener)
    }*/
}