package com.example.alarmmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.util.Log
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    private var ringtone: Ringtone? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmId = intent?.getIntExtra("alarmId", -1) ?: -1
        Log.d("AlarmReceiver", "Triggering alarm with ID: $alarmId")
        // Display toast when the alarm is triggered
        Toast.makeText(context, "Alarm triggered!", Toast.LENGTH_SHORT).show()

        // Play the alarm ringtone when the alarm triggers
        context?.let {
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            if (ringtone == null) {
                ringtone = RingtoneManager.getRingtone(it, alarmUri)
            }

            // Play the ringtone if it's not already playing
            ringtone?.play()

            // Send a broadcast to notify that the alarm is triggered
            val alarmIntent = Intent("com.example.alarmmanager.ALARM_TRIGGERED")
            alarmIntent.putExtra("alarmId", intent?.getIntExtra("alarmId", -1) ?: -1)
            context.sendBroadcast(alarmIntent)
        }
    }

    // Call this method to stop the ringtone
    fun stopRingtone() {
        ringtone?.stop()
        ringtone = null  // Release the ringtone object after stopping it
    }
}
