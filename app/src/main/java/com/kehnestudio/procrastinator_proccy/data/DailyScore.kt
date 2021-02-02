package com.kehnestudio.procrastinator_proccy.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.LocalTime
import java.time.OffsetDateTime
import java.util.*

@Entity(tableName = "score_table")
data class DailyScore(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val dailyScore: Int,
    val date: Calendar,
    val userId: String
)
