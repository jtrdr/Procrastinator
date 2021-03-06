package com.kehnestudio.procrastinator_proccy.data.offline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.kehnestudio.procrastinator_proccy.data.online.ScoreHistoryFirestore
import com.kehnestudio.procrastinator_proccy.ui.progress.ProgressViewModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table WHERE userId = :uid")
    fun getSpecificUser(uid: String): LiveData<User>

    @Transaction
    @Query("SELECT SUM(score) FROM score_table where userId =:uid")
    fun getSumOfDailyScore(uid: String): LiveData<Long>

    @Query("SELECT userId FROM user_table")
    fun getUserId(): LiveData<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Transaction
    @Query("SELECT score FROM score_table where userId =:uid AND date =:date")
    fun getSpecificDailyScore(uid:String, date: Date): LiveData<Int>

    @Transaction
    @Query ("SELECT score FROM score_table where date =:date")
    fun getDailyScore(date: Date): Int

    @Transaction
    @Query("SELECT date, score FROM score_table")
    fun getDailyScores() : Flow<List<ScoreHistoryLocalDate>>

    @Transaction
    @Query ("SELECT date FROM score_table")
    fun getDailyScoreHistory(): LiveData<List<LocalDate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(scoreHistory: ScoreHistory)

    @Update
    suspend fun updateScore(scoreHistory: ScoreHistory)

    @Query("DELETE FROM user_table WHERE userId =:uid")
    suspend fun deleteAllUsers(uid: String)

    @Query("DELETE FROM score_table WHERE userId =:uid")
    suspend fun deleteAllHistory(uid: String)

    /**
     * FIRESTORE
     */

    @Transaction
    @Query("SELECT date, score FROM score_table")
    fun getDailyScoreHistoryFireStore(): List<ScoreHistoryFirestore>

    @Query("SELECT * FROM user_table")
    fun getSpecificUserFireStore(): User

}