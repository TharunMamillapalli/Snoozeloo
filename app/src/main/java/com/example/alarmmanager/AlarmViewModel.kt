package com.example.alarmmanager

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Calendar

class AlarmViewModel(application: Application):AndroidViewModel(application) {
    private val alarmDao = AlarmDatabase.getDatabase(application).alarmDao()
    val alarms: LiveData<List<Alarm>> = alarmDao.getAllAlarms()

    fun addAlarm(newAlarm: Alarm) {
        viewModelScope.launch {
            alarmDao.insertAlarm(newAlarm)
            Log.d("AlarmViewModel", "Inserted: $newAlarm")
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            alarmDao.deleteAlarm(alarm)
        }
    }
    fun updateAlarmStatus(alarm: Alarm, isActive: Boolean) {
        viewModelScope.launch {
            // Update the isActive field of the alarm
            val updatedAlarm = alarm.copy(isActive = isActive)
            alarmDao.updateAlarm(updatedAlarm)  // Assuming you have an update method in your DAO
            Log.d("AlarmViewModel", "Updated: $updatedAlarm")
        }
    }

    fun updateRemainingTime(alarm: Alarm) {
        viewModelScope.launch {
            val alarmTime = getAlarmTimeInMillis(alarm.time)
            val remainingMillis = alarmTime - System.currentTimeMillis()

            // Check if the alarm is in the future
            if (remainingMillis > 0) {
                val hours = remainingMillis / (1000 * 60 * 60)
                val minutes = (remainingMillis % (1000 * 60 * 60)) / (1000 * 60)
                val timeString = "$hours h $minutes min"

                // Update the alarm with the calculated remaining time
                val updatedAlarm = alarm.copy(remainingTime = timeString)
                alarmDao.updateAlarm(updatedAlarm)
            } else {
                // If the alarm is already past, show "Expired" or similar
                val updatedAlarm = alarm.copy(remainingTime = "")
                alarmDao.updateAlarm(updatedAlarm)
            }
        }
    }


    private fun getAlarmTimeInMillis(time: String): Long {
        val calendar = Calendar.getInstance()
        val timeParts = time.split(":")
        calendar.set(Calendar.HOUR_OF_DAY, timeParts[0].toInt())
        calendar.set(Calendar.MINUTE, timeParts[1].toInt())
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }
}