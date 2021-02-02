package com.kehnestudio.procrastinator_proccy.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface UserDao {

    @Query("SELECT displayName FROM user_table WHERE uid = :userId")
    fun getUserName(userId: String): String

    @Query("SELECT totalScore FROM user_table WHERE uid =:userId")
    fun getTotalScore(userId:String): Int

    @Query("SELECT uid FROM user_table WHERE uid =:userId")
    fun getUUID(userId:String): Int



    @Transaction
    @Query("SELECT * FROM user_table")
    fun getUsersWithDailyScores(): LiveData<List<UserWithDailyScores>>


    //suspend: switching to another thread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    //suspend: switching to another thread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailySore(dailyScore: DailyScore)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)
}