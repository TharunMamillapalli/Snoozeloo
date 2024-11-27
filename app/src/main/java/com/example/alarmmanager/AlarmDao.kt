package com.example.alarmmanager

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AlarmDao {

    @Insert(onConflict =OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: Alarm)
    @Query("SELECT * FROM alarms")
    fun getAllAlarms():LiveData<List<Alarm>>
    @Delete
    suspend fun deleteAlarm(alarm: Alarm)
    @Update
    suspend fun updateAlarm(alarm: Alarm)
}