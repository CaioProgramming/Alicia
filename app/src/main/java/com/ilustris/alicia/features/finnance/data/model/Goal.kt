package com.ilustris.alicia.features.finnance.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val value: Double,
    val description: String,
    val createdAt: Long,
    val tag: Tag,
    val isComplete: Boolean = false,
    val completedAt: Long = 0L,
)
