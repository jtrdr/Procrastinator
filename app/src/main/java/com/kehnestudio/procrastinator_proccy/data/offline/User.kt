package com.kehnestudio.procrastinator_proccy.data.offline

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "user_table")
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = false) var userId: String,
    val name: String,
    val totalScore: Int? = null
) : Parcelable
