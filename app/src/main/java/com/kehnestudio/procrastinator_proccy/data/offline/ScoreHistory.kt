package com.kehnestudio.procrastinator_proccy.data.offline

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "score_table")
data class ScoreHistory(
    @PrimaryKey val date: LocalDate,
    val userId: String,
    val score: Long? = null
)
