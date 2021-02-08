package com.kehnestudio.procrastinator_proccy.data.offline

import androidx.room.Embedded
import androidx.room.Relation


data class UserWithScoreHistory(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val scoreHistory : List<ScoreHistory>
)
