package com.ilustris.alicia.features.finnance.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ilustris.alicia.utils.emptyString
import java.util.Calendar

@Entity
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val value: Double = 0.0,
    val name: String = emptyString(),
    val createdAt: Long = Calendar.getInstance().timeInMillis,
    val tag: String = Tag.UNKNOWN.name,
    val isComplete: Boolean = false,
    val completedAt: Long = 0L,
    val badge: Int = 0,
)
