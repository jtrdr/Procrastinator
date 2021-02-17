package com.kehnestudio.procrastinator_proccy.repositories

import com.kehnestudio.procrastinator_proccy.data.offline.ScoreHistory
import com.kehnestudio.procrastinator_proccy.data.offline.User
import com.kehnestudio.procrastinator_proccy.data.offline.UserDao
import java.util.*
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val userDao: UserDao
) {


    fun getDailyScores() = userDao.getDailyScores()

    fun getSpecificUser(uid: String) = userDao.getSpecificUser(uid)

    fun getSpecificDailyScore(uid:String, date: Date) = userDao.getSpecificDailyScore(uid, date)

    fun getUserWithScoreHistoryFirestore() = userDao.getSpecificUserFireStore()

    fun getDailyScoreHistoryForFireStore() = userDao.getDailyScoreHistoryFireStore()

    fun getDailyScore(date: Date) = userDao.getDailyScore(date)

    fun getSumOfDailyScore(uid: String) = userDao.getSumOfDailyScore(uid)

    fun getUserId() = userDao.getUserId()

    suspend fun insertScore(scoreHistory: ScoreHistory) {
        userDao.insertScore(scoreHistory)
    }

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun delete(uid: String) {
        userDao.deleteAllHistory(uid)
        userDao.deleteAllUsers(uid)
    }

}