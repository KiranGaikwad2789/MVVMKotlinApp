package com.example.mvvmkotlinapp.repository.room

import androidx.room.*

@Dao
interface StartDutyStatusDao {

    @Query("SELECT * FROM StartDutyStatus")
    fun getStatus(): StartDutyStatus

    @Query("SELECT * FROM StartDutyStatus WHERE status IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<StartDutyStatus>

    @Query("SELECT * FROM StartDutyStatus WHERE status LIKE :status AND " + "time LIKE :time LIMIT 1")
    fun findByName(status: String, time: String): List<StartDutyStatus>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStatus(vararg status: StartDutyStatus)

    @Delete
    fun delete(user: StartDutyStatus)
}