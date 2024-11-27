package com.example.alarmmanager

import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.alarmmanager.databinding.FragmentStopAlarmBinding

class StopAlarmFragment : Fragment() {

    private lateinit var binding: FragmentStopAlarmBinding
    private var alarmId: Int? = null
    private var ringtone: android.media.Ringtone? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStopAlarmBinding.inflate(inflater, container, false)

        // Retrieve the alarmId passed from MainActivity or AlarmReceiver
        alarmId = arguments?.getInt("alarmId")
        Log.d("AlarmReceiver","Received alarmId : $alarmId")

        // Setup the "Turn Off" button to stop the alarm
        binding.btnFragStop.setOnClickListener {
            stopAlarm()
        }

        // Play a sound when the alarm triggers
        playRingtone()

        return binding.root
    }

    // Function to play the ringtone when the alarm triggers
    private fun playRingtone() {
        context?.let {
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ringtone = RingtoneManager.getRingtone(it, alarmUri)

            // Play the ringtone
            ringtone?.play()
        }
    }

    // Function to stop the alarm sound and perform necessary actions
    private fun stopAlarm() {
        ringtone?.stop()
        // Optionally, you can add logic to reset the alarm state or update any UI.
        Toast.makeText(context, "Alarm stopped!", Toast.LENGTH_SHORT).show()

        // You can also add additional logic like navigating back to the previous fragment or updating the alarm's state
        // if necessary.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up ringtone when the fragment view is destroyed
        ringtone?.stop()
    }
}
