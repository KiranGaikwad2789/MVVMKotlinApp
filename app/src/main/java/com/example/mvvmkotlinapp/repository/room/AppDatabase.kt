package com.example.mvvmkotlinapp.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(User::class,CurrentLocation::class), version = 1)
abstract class AppDatabase : RoomDatabase() {


    abstract fun locationDao(): CurrentLocationDao
    abstract fun userDao(): UserDao
    abstract fun cityDao(): CityDao
    //abstract fun featureDao(): FeatureDao


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}