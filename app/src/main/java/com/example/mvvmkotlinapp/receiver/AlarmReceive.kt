package com.example.mvvmkotlinapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.mvvmkotlinapp.services.LocationTrackingService

class AlarmReceive: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.e("Service_call_", "You are in AlarmReceive class.")
        val background = Intent(context, LocationTrackingService::class.java)
        context.startService(background)
    }
}