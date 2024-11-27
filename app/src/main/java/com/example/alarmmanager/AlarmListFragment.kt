package com.example.alarmmanager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alarmmanager.databinding.FragmentAlarmListBinding
import java.util.Calendar


class AlarmListFragment : Fragment() {

    private lateinit var binding:FragmentAlarmListBinding
    private val alarmViewModel: AlarmViewModel by activityViewModels()
    private lateinit var alarmAdapter: AlarmAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_alarm_list, container, false)
        binding=FragmentAlarmListBinding.inflate(layoutInflater,container,false)
        // Initialize the adapter with an empty list
        // Initialize the adapter with the required context and the toggle callback
        alarmAdapter = AlarmAdapter(requireContext()) { alarm, isChecked ->
            // Handle the alarm toggle action here
            alarmViewModel.updateAlarmStatus(alarm, isChecked)  // Example method to update alarm status
        }
        binding.recyclerViewAlarms.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewAlarms.adapter = alarmAdapter

        return binding.root






    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Observe the alarms from the ViewModel
        alarmViewModel.alarms.observe(viewLifecycleOwner) { alarmList ->
            Log.d("AlarmList", "Alarms: $alarmList")
            alarmAdapter.submitList(alarmList)  // Update the adapter with new alarms
            alarmList.forEach { alarm ->
                alarmViewModel.updateRemainingTime(alarm)  // Update remaining time for each alarm
            }
        }

        binding.AddAlarm.setOnClickListener{
            findNavController().navigate(R.id.action_alarmListFragment_to_createAlarmFragment)
        }

        // Observe data passed from CreateAlarmFragment using SavedStateHandle
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Alarm>("newAlarm")
            ?.observe(viewLifecycleOwner) { newAlarm ->
                alarmViewModel.addAlarm(newAlarm)  // Add the new alarm to the ViewModel
            }
    }




}