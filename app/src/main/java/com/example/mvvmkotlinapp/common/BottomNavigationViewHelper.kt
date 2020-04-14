package com.example.mvvmkotlinapp.common

import android.annotation.SuppressLint
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.reflect.Field


class BottomNavigationViewHelper {

    companion object{

        @SuppressLint("RestrictedApi")
        fun removeShiftMode(view: BottomNavigationView) {
            val menuView = view.getChildAt(0) as BottomNavigationMenuView
            try {
                val shiftingMode: Field = menuView.javaClass.getDeclaredField("mShiftingMode")
                shiftingMode.setAccessible(true)
                shiftingMode.setBoolean(menuView, false)
                shiftingMode.setAccessible(false)
                for (i in 0 until menuView.childCount) {
                    val item =
                        menuView.getChildAt(i) as BottomNavigationItemView
                    item.setShifting(false)
                    val shiftAmount: Field = item.javaClass.getDeclaredField("mShiftAmount")
                    shiftAmount.setAccessible(true)
                    shiftAmount.setInt(item, 0)
                    shiftAmount.setAccessible(false)
                    item.setChecked(item.itemData.isChecked)
                }
            } catch (e: NoSuchFieldException) {
                Log.e("", "Unable to get fields")
            } catch (e: IllegalAccessException) {
                //Timber.e(e, "Unable to change values")
            }
        }

    }
}