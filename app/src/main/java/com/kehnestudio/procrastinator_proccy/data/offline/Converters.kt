package com.kehnestudio.procrastinator_proccy.data.offline

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class Converters {


    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(millisSinceEpoch: Long?): LocalDate?{
        return millisSinceEpoch.let {
            Instant.ofEpochMilli(it!!).atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }

}