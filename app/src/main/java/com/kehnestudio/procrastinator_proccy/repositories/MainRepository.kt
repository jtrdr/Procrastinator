package com.kehnestudio.procrastinator_proccy.repositories

import com.kehnestudio.procrastinator_proccy.data.*
import javax.inject.Inject

class MainRepository @Inject constructor(
    val userDao: UserDao,
    val dailyScoreDao: DailyScoreDao
) {
    suspend fun insertUser(user: User) = userDao.insert(user)

    suspend fun insertDailyScore(dailyScore: DailyScore) = userDao.insertDailySore(dailyScore)

    suspend fun deleteUser(user: User) = userDao.delete(user)

    suspend fun updateUser(user: User) = userDao.update(user)

    fun getUserWithDailyScore() = userDao.getUsersWithDailyScores()

    fun getDisplayName(userId: String) = userDao.getUserName(userId)

    fun getTotalScore(userId:String) = userDao.getTotalScore(userId)

    fun getUUID(userId: String) =userDao.getUUID(userId)

}
