package com.example.mvvmkotlinapp.common

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


public class DateTime {
//https://gist.github.com/maiconhellmann/796debb4007139d50e39f139be08811c

    /*val dateTime = LocalDateTime.now()
    println(dateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)))
    println(dateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)))
    println(dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)))
    println(dateTime.format(DateTimeFormatter.ofPattern("M/d/y H:m:ss")))*/

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

    @RequiresApi(Build.VERSION_CODES.O)
    public fun getSelectedDateForamt(selectedDate:String): String? {

        var dateSplit=selectedDate.split(" ")

        val parsedDate = LocalDate.parse(dateSplit[0], DateTimeFormatter.ISO_LOCAL_DATE)
        val formattedDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(parsedDate)

        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val outputFormat: DateFormat = SimpleDateFormat("EEE, d MMM yyyy','KK:mm a")
        System.out.println(outputFormat.format(inputFormat.parse(selectedDate)))

        return outputFormat.format(inputFormat.parse(selectedDate))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public fun chatDateForamt(selectedDate:String): String? {

        var dateSplit=selectedDate.split(" ")

        val parsedDate = LocalDate.parse(dateSplit[0], DateTimeFormatter.ISO_LOCAL_DATE)
        val formattedDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(parsedDate)

        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val outputFormat: DateFormat = SimpleDateFormat("KK:mm a")
        System.out.println(outputFormat.format(inputFormat.parse(selectedDate)))

        return outputFormat.format(inputFormat.parse(selectedDate))
    }
}