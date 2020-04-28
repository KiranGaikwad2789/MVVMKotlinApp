package com.example.mvvmkotlinapp.common

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

public class DateTime {

    @RequiresApi(Build.VERSION_CODES.O)
    public fun getDateTime():String{

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)
        return  formatted
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public fun getDateFormater():String{

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss")
        val formatted = current.format(formatter)
        return  formatted
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public fun orderDateFormater():String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm")
        val formatted = current.format(formatter)
        return  formatted
    }
}