package com.kehnestudio.procrastinator_proccy.data

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithDailyScores(
    @Embedded val user: User,
    @Relation(
        parentColumn = "uid",
        entityColumn = "userId"
    )
    val dailyScores: List<DailyScore>
)
