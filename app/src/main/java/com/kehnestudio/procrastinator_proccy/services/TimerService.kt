package com.kehnestudio.procrastinator_proccy.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.kehnestudio.procrastinator_proccy.Constants.ACTION_SHOW_GOALS_FRAGMENT
import com.kehnestudio.procrastinator_proccy.Constants.ACTION_START_SERVICE
import com.kehnestudio.procrastinator_proccy.Constants.ACTION_STOP_SERVICE
import com.kehnestudio.procrastinator_proccy.Constants.NOTIFICATION_CHANNEL_ID
import com.kehnestudio.procrastinator_proccy.Constants.NOTIFICATION_CHANNEL_NAME
import com.kehnestudio.procrastinator_proccy.Constants.NOTIFICATION_ID
import com.kehnestudio.procrastinator_proccy.R
import com.kehnestudio.procrastinator_proccy.TimerUtility
import com.kehnestudio.procrastinator_proccy.ui.MainActivity
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.fixedRateTimer

class TimerService : LifecycleService() {

    var isTimerRunning = false

    private val timeLeftInMinutes = MutableLiveData<Long>()

    companion object {
        val mTimerIsRunning = MutableLiveData<Boolean>()
        val timeLeftInMillies = MutableLiveData<Long>()
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                ACTION_START_SERVICE -> {
                    if (!isTimerRunning) {
                        startForeGroundService()
                        Timber.d("onStartCommand: Service started")
                        isTimerRunning = true
                    } else {
                        Timber.d("Already running")
                    }
                }
                ACTION_STOP_SERVICE -> {
                    stopService()
                    Timber.d("onStartCommand: Service stopped")

                }
                else -> Timber.d("onStartCommand: Service did nothing")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForeGroundService() {

        startTimer()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_sloth_svg)
            .setContentTitle("Timer")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun postInitialValues() {
        mTimerIsRunning.postValue(false)
        timeLeftInMillies.postValue(5000L)
    }

    private var mTimerRunning = false
    private var timerDuration = 1L


    private fun startTimer() {
        mTimerRunning = true
        mTimerIsRunning.postValue(true)
        var postedTime: Long

        CoroutineScope(Dispatchers.Main).launch {
            val totalSeconds = TimeUnit.MINUTES.toSeconds(timerDuration)
            val tickSeconds = 1
            for (second in totalSeconds downTo tickSeconds) {
                val time = String.format("%02d:%02d",
                    TimeUnit.SECONDS.toMinutes(second),
                    second - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(second))
                )
                Timber.d(time)
                postedTime = TimeUnit.SECONDS.toMillis(second)
                timeLeftInMillies.postValue(postedTime)
                delay(1000)
            }
            Timber.d("Done")
        }

    }



    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_GOALS_FRAGMENT
        },
        FLAG_UPDATE_CURRENT
    )

    private fun stopService() {
        mTimerRunning = false
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

}