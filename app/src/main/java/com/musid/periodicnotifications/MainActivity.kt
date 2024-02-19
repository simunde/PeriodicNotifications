package com.musid.periodicnotifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.musid.periodicnotifications.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {


    lateinit var mainBinding: ActivityMainBinding
    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY) // Correctly gets the current hour
    val currentMinute = calendar.get(Calendar.MINUTE) // Correctly gets the current minute

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        mainBinding.sendNotifications.setOnClickListener {


            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(currentHour)
                .setMinute(currentMinute)
                .setTitleText("Set Notifications Time")
                .build()

            timePicker.show(supportFragmentManager,"1")
            timePicker.addOnPositiveButtonClickListener {
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.hour)
                calendar.set(Calendar.MINUTE,timePicker.minute)
                calendar.set(Calendar.SECOND,0)
                calendar.set(Calendar.MILLISECOND,0)

                val intent = Intent(applicationContext,NotificationReceiver::class.java)

                val pendingIntent = if (Build.VERSION.SDK_INT>=23){
                    PendingIntent.getBroadcast(
                        applicationContext,
                        100,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )}
                else{
                    PendingIntent.getBroadcast(
                        applicationContext,
                        100,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }

                val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent)

                    Toast.makeText(applicationContext,"The alarm has been set", Toast.LENGTH_LONG).show()
                }


            }
        }



    }
