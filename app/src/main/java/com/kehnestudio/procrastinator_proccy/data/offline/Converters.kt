package com.kehnestudio.procrastinator_proccy.data.offline

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {

    /*
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(date: Long?) : LocalDate? {
        return toLocalDate(date)
    }

     */

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLongDate(dateLong: Long?): LocalDate? {
        return if (dateLong == null) {
            null
        } else {
            LocalDate.ofEpochDay(dateLong)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toDateLong(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

}