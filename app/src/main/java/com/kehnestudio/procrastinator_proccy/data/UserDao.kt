package com.kehnestudio.procrastinator_proccy.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table")
    fun getUser(): LiveData<List<User>>

    @Query("SELECT * FROM user_table WHERE id =:uid")
    fun getSpecificUser(uid: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

}