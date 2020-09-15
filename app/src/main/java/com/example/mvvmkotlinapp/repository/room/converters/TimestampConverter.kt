package com.example.mvvmkotlinapp.repository.room.converters

import android.provider.SyncStateContract
import androidx.room.TypeConverter
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TimestampConverter {

    var df: DateFormat = SimpleDateFormat("YYYY-MM-DD HH:MM:SS")
    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return if (value != null) {
            try {
                return df.parse(value)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            null
        } else {
            null
        }
    }
}