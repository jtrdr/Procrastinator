package com.kehnestudio.procrastinator_proccy

import timber.log.Timber
import java.util.concurrent.TimeUnit

object TimerUtility {

    fun getFormattedTimerTime(ms: Long, includeMillis: Boolean = false): String {
        var milliseconds = ms
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        var seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        if(includeMillis){
            return "${if(minutes < 10) "0" else ""}$minutes:" +
                    "${if(seconds < 10) "0" else ""}$seconds"
        } else {
            return minutes.toString()
        }
    }
}