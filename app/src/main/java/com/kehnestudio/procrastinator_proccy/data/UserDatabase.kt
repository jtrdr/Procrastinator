package com.kehnestudio.procrastinator_proccy.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kehnestudio.procrastinator_proccy.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [User::class, DailyScore::class], version = 1)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun dailyScoreDao(): DailyScoreDao

}

class Converters {
    @TypeConverter
    fun fromTimeMillis(millis: Long): Calendar {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis

        return cal
    }

    @TypeConverter
    fun toTimeMillis(cal: Calendar): Long {
        return cal.timeInMillis
    }
}