package com.kehnestudio.procrastinator_proccy.data.offline

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_table")
data class ScoreHistory(
    @PrimaryKey val date: String,
    val userId: String,
    val score: Int? = null
)
