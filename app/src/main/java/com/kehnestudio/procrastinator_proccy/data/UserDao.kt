package com.kehnestudio.procrastinator_proccy.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table WHERE id = :uid")
    fun getSpecificUser(uid: String): LiveData<User>

    @Query("SELECT name FROM user_table WHERE id = :uid")
    fun getUserName(uid: String): LiveData<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

}