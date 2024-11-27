package com.example.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.alarmmanager.databinding.FragmentCreateAlarmBinding
import java.util.Calendar


class CreateAlarmFragment : Fragment() {
    private lateinit var binding:FragmentCreateAlarmBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_create_alarm, container, false)
        binding=FragmentCreateAlarmBinding.inflate(layoutInflater,container,false)
        // Set up save button click listener
        binding.btnSaveFrag.setOnClickListener {
            saveAlarm()
        }

        binding.btnCancelFrag.setOnClickListener {
            findNavController().popBackStack()
        }
        setUpTimeChangeListeners()
        // Set up the switch state change listener


        return binding.root



    }

    private fun saveAlarm() {
        // Get input values from EditText views
        val hour = binding.etHour.text.toString().toIntOrNull() ?: 0
        val minute = binding.etMinute.text.toString().toIntOrNull() ?: 0
        val alarmName = binding.etAlarmName.text.toString()


        // Create Alarm object
        val newAlarm = Alarm(
            name = alarmName,
            time = "$hour:$minute",
            remainingTime = caluculateRemainingTime(hour, minute),
            isActive = false)

        // Pass alarm data back to AlarmListFragment using SavedStateHandle
        findNavController().previousBackStackEntry?.savedStateHandle?.set("newAlarm", newAlarm)

        // Navigate back to AlarmListFragment
        findNavController().popBackStack()
    }
    private fun caluculateRemainingTime(hour:Int,minute:Int):String{
        val currentTime= Calendar.getInstance()
        val alarmTime= Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY,hour)
            set(Calendar.MINUTE, minute)
        }
        if(alarmTime.before(currentTime)){
            alarmTime.add(Calendar.DAY_OF_YEAR,1)
        }
        val diffMillis = alarmTime.timeInMillis - currentTime.timeInMillis
        val diffHours = (diffMillis / (1000 * 60 * 60)).toInt()
        val diffMinutes = ((diffMillis / (1000 * 60)) % 60).toInt()

        return "Alarm in $diffHours hour(s) and $diffMinutes minute(s)"
    }

    private fun setUpTimeChangeListeners(){
        val updateTimeText = {
            val hour = binding.etHour.text.toString().toIntOrNull() ?: 0
            val minute = binding.etMinute.text.toString().toIntOrNull() ?: 0
            val remainingTime = caluculateRemainingTime(hour, minute)
            binding.tvTimeInstruction.text=remainingTime // Update the TextView
        }
        binding.etHour.addTextChangedListener{
            updateTimeText()
        }
        binding.etMinute.addTextChangedListener {
            updateTimeText()
        }
    }







}