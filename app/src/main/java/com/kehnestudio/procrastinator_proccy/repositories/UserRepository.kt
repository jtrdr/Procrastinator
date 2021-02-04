package com.kehnestudio.procrastinator_proccy.repositories

import com.kehnestudio.procrastinator_proccy.data.User
import com.kehnestudio.procrastinator_proccy.data.UserDao

import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    val users = userDao.getUser()

    suspend fun insert(user: User){
        userDao.insert(user)
    }

    suspend fun update(user: User){
        userDao.update(user)
    }

    suspend fun delete(user: User){
        userDao.delete(user)
    }

}