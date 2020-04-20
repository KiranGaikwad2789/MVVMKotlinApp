package com.example.mvvmkotlinapp.repository.room

import androidx.lifecycle.LiveData
import androidx.room.*



@Dao
interface FeatureDao {

    @Query("SELECT * FROM Features")
    fun getFeatureList(): LiveData<List<Features>>

    @Query("SELECT * FROM Features WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Features>

    @Query("SELECT * FROM Features WHERE featureId LIKE :featureId AND " + "featureName LIKE :featureName LIMIT 1")
    fun findByName(featureId: String, featureName: String): List<Features>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg users: Features)

    @Query("DELETE FROM Features")
    fun deleteTableAllEntry()

}