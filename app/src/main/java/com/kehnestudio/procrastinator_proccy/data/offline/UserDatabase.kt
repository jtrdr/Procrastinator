package com.kehnestudio.procrastinator_proccy.data.offline

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, ScoreHistory::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}
