package com.ilustris.alicia.features.finnance.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val value: Double,
    val name: String,
    val createdAt: Long,
    val tag: String,
    val isComplete: Boolean = false,
    val completedAt: Long = 0L,
    val badge: Int = 0,
) {
    fun promptDescription() = "goal $name with value $value at $tag"
}
