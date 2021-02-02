package com.kehnestudio.procrastinator_proccy.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Dao
interface DailyScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dailyScore: DailyScore)

    @Update
    suspend fun update(dailyScore: DailyScore)

    @Delete
    suspend fun delete(dailyScore: DailyScore)

}