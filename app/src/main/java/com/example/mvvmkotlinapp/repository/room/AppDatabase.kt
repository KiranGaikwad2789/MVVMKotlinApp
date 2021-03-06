package com.example.mvvmkotlinapp.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.repository.room.converters.TimestampConverter
import com.example.mvvmkotlinapp.repository.room.dao.*
import com.example.mvvmkotlinapp.repository.room.tables.MasterProductOrder
import com.example.mvvmkotlinapp.repository.room.tables.OutletDetails
import com.example.mvvmkotlinapp.repository.room.tables.Product
import com.example.mvvmkotlinapp.repository.room.tables.ProductCategory


@Database(entities = arrayOf(User::class,CurrentLocation::class,StartDutyStatus::class,Features::class,
    City::class,Route::class,Outlet::class,Distributor::class,ProductCategory::class,Product::class,
    ProductOrderModel::class, MasterProductOrder::class,OutletDetails::class), version = 1)
@TypeConverters(DataConverter::class,RouteListConverter::class,ProductListConverter::class,TimestampConverter::class)
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
    abstract fun productOrderDao(): ProductOrderDao
    abstract fun masterProductOrderDao(): MasterProductOrderDao
    abstract fun outletDetailsDao(): OutletDetailsDao




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