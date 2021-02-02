package com.kehnestudio.procrastinator_proccy.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = false) val uid: String,
    val displayName: String,
    val totalScore: Int
)
