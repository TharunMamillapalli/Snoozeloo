package com.example.alarmmanager

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "alarms")
data class Alarm (
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val name:String,
    val time:String,
    var remainingTime:String="",
    var isActive:Boolean):Parcelable{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Alarm) return false
        return name == other.name &&
                time == other.time &&
                isActive == other.isActive &&
                remainingTime == other.remainingTime
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + isActive.hashCode()
        result = 31 * result + remainingTime.hashCode()
        return result
    }
}