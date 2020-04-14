package com.example.mvvmkotlinapp.common

import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


@BindingMethods(value = [BindingMethod(type = BindingAdaptersNavigation::class,
    attribute = "app:onNavigationItemSelected", method = "setOnNavigationItemSelectedListener")])
object BindingAdaptersNavigation {

    //                app:onNavigationItemSelected="@{homeModel::onNavigationItemSelected}"

    @BindingAdapter("onNavigationItemSelected")
    @JvmStatic
    fun setOnNavigationItemSelectedListener(
        view: NavigationView, listener: NavigationView.OnNavigationItemSelectedListener?
    ) {
        view.setNavigationItemSelectedListener(listener)
    }
}