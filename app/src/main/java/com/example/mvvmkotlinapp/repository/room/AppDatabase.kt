package com.example.mvvmkotlinapp.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mvvmkotlinapp.repository.room.dao.*


@Database(entities = arrayOf(User::class,CurrentLocation::class,StartDutyStatus::class,Features::class,
    City::class,Route::class,Outlet::class,Distributor::class), version = 1)
@TypeConverters(DataConverter::class,RouteListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun locationDao(): CurrentLocationDao
    abstract fun startDutyStatusDao(): StartDutyStatusDao
    abstract fun userDao(): UserDao
    abstract fun cityDao(): CityDao
    abstract fun featureDao(): FeatureDao
    abstract fun routeDao(): RouteDao
    abstract fun outletDao(): OutletDao
    abstract fun distributorDao(): DistributorDao
    abstract fun productCatDao(): ProductCategoryDao
    abstract fun productDao(): ProductDao


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