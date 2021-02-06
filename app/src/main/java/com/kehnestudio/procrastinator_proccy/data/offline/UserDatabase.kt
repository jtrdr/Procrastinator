package com.kehnestudio.procrastinator_proccy.data.offline

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [User::class, ScoreHistory::class], version = 1)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}
