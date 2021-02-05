package com.kehnestudio.procrastinator_proccy.data.offline

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table WHERE userId = :uid")
    fun getSpecificUser(uid: String): LiveData<User>

    /** Examples
    @Query("SELECT name FROM user_table WHERE id = :uid")
    fun getUserName(uid: String): LiveData<String>

    @Query("SELECT totalScore FROM user_table WHERE id = :uid")
    fun getTotalScore(uid: String): LiveData<Int>
     **/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Transaction
    @Query("SELECT * FROM user_table where userId =:uid")
    fun getUserWithScoreHistory(uid: String): LiveData<List<UserWithScoreHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(scoreHistory: ScoreHistory)

    @Update
    suspend fun updateScore(scoreHistory: ScoreHistory)

    @Query("DELETE FROM user_table")
    suspend fun deleteAllUsers()

    @Query("DELETE FROM score_table")
    suspend fun deleteAllHistory()
}