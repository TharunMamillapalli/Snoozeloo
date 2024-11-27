package com.example.alarmmanager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmmanager.databinding.ItemAlarmBinding
import java.util.Calendar

class AlarmAdapter(
    private val context: Context,
    private val onAlarmToggled: (Alarm, Boolean) -> Unit
) : ListAdapter<Alarm, AlarmAdapter.AlarmViewHolder>(AlarmDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = getItem(position)
        holder.bind(alarm, position)
    }

    inner class AlarmViewHolder(private val binding: ItemAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(alarm: Alarm, position: Int) {
            binding.apply {
                // Bind alarm data to views
                textViewTime.text = alarm.time
                textViewName.text = alarm.name
                tvInfo.text = alarm.remainingTime

                // Reset listener to avoid recycling issues
                btnOnOff.setOnCheckedChangeListener(null)

                // Set button state
                btnOnOff.isChecked = alarm.isActive

                // Reattach listener for toggling the alarm
                btnOnOff.setOnCheckedChangeListener { _, isChecked ->
                    // Update alarm state and trigger callbacks
                    alarm.isActive = isChecked
                    onAlarmToggled(alarm, isChecked)
                    Log.d("AlarmAdapter", "Toggling Alarm: ${alarm.name}, isActive: $isChecked")

                    if (isChecked) {
                        setAlarm(alarm, position)
                    } else {
                        cancelAlarm(alarm, position)
                    }

                    // Refresh the updated list
                    val updatedList = currentList.toMutableList()
                    updatedList[position] = alarm
                    submitList(updatedList.toList())
                }
            }
        }
    }

    private fun getAlarmTimeInMillis(hour: Int, minute: Int): Long {
        val currentTime = Calendar.getInstance()
        val alarmTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Schedule for the next day if the time is in the past
        if (alarmTime.before(currentTime)) {
            alarmTime.add(Calendar.DAY_OF_YEAR, 1)
        }
        return alarmTime.timeInMillis
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm(alarm: Alarm, position: Int) {
        val timeParts = alarm.time.split(":")
        if (timeParts.size == 2) {
            val hour = timeParts[0].toIntOrNull() ?: return
            val minute = timeParts[1].toIntOrNull() ?: return
            val alarmTimeInMillis = getAlarmTimeInMillis(hour, minute)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context, position, intent, PendingIntent.FLAG_IMMUTABLE
            )

            // Schedule the alarm
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTimeInMillis,
                pendingIntent
            )
        }
    }

    private fun cancelAlarm(alarm: Alarm, position: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, position, intent, PendingIntent.FLAG_IMMUTABLE
        )

        // Cancel the alarm
        alarmManager.cancel(pendingIntent)
    }

    class AlarmDiffCallback : DiffUtil.ItemCallback<Alarm>() {
        override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            // Include all relevant fields for proper comparison
            return oldItem == newItem
        }
    }
}
