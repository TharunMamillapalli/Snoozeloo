package com.example.alarmmanager

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(entities = [Alarm::class], version = 3, exportSchema = false)
abstract class AlarmDatabase:RoomDatabase() {
    abstract fun alarmDao():AlarmDao

    companion object{

        private var INSTANCE:AlarmDatabase?=null

        fun getDatabase(context: Context):AlarmDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmDatabase::class.java,
                    "alarm_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                Log.d("AlarmDatabase", "Database initialized")
                instance
            }
        }

    }

}