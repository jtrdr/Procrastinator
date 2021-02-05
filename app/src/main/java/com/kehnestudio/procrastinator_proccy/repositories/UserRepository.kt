package com.kehnestudio.procrastinator_proccy.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kehnestudio.procrastinator_proccy.data.User
import com.kehnestudio.procrastinator_proccy.data.UserDao
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val userDao: UserDao
) {


    fun getUserName(uid: String) = userDao.getUserName(uid)
    fun getSpecificUser(uid: String) = userDao.getSpecificUser(uid)

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