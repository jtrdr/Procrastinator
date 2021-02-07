package com.kehnestudio.procrastinator_proccy.repositories

import com.kehnestudio.procrastinator_proccy.data.offline.ScoreHistory
import com.kehnestudio.procrastinator_proccy.data.offline.User
import com.kehnestudio.procrastinator_proccy.data.offline.UserDao
import java.time.LocalDate
import java.util.*
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val userDao: UserDao
) {

    fun getSpecificUser(uid: String) = userDao.getSpecificUser(uid)

    fun getSpecificDailyScore(uid:String, date: Date) = userDao.getSpecificDailyScore(uid, date)

    fun getUserWithScoreHistoryFirestore(uid: String) = userDao.getSpecificUserFireStore(uid)

    fun getDailyScoreHistoryForFireStore(uid: String) = userDao.getDailyScoreHistory(uid)

    fun getSumOfDailyScore(uid: String) = userDao.getSumOfDailyScore(uid)

    suspend fun insertScore(scoreHistory: ScoreHistory) {
        userDao.insertScore(scoreHistory)
    }

    suspend fun updateScore(scoreHistory: ScoreHistory) {
        userDao.updateScore(scoreHistory)
    }

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun update(user: User) {
        userDao.update(user)
    }

    suspend fun delete() {
        userDao.deleteAllHistory()
        userDao.deleteAllUsers()
    }

}