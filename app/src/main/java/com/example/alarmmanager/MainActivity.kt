package com.example.alarmmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.alarmmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var alarmTriggeredReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialise viewBinding
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set up navigationController
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController=navHostFragment.navController

        // Register the BroadcastReceiver for alarm triggered
        alarmTriggeredReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val alarmId = intent?.getIntExtra("alarmId", -1)
                Log.d("AlarmReceiver", "Received alarmId: $alarmId")
                if (alarmId != null && alarmId != -1) {
                    // Navigate to the StopAlarmFragment when the alarm is triggered
                    val bundle = Bundle().apply {
                        putInt("alarmId", alarmId)
                    }
                    Log.d("AlarmReceiver", "Navigating to StopAlarmFragment with alarmId:$alarmId")
                    navController.navigate(R.id.stopAlarmFragment, bundle)
                }else {
                    Log.d("AlarmReceiver", "Invalid alarmId, navigation skipped")
                }
            }

        }
        // Register the receiver to listen for the alarm triggered broadcast
        val filter = IntentFilter("com.example.alarmmanager.ALARM_TRIGGERED")
        registerReceiver(alarmTriggeredReceiver, filter)



    }
    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver when the activity is destroyed to prevent memory leaks
        unregisterReceiver(alarmTriggeredReceiver)
    }
}