package com.kehnestudio.procrastinator_proccy.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "user_table")
@Parcelize
data class User(
    val name: String,
    val totalScore: Int,
    @PrimaryKey(autoGenerate = false) val id: String
) : Parcelable
