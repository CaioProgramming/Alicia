package com.ilustris.alicia.features.finnance.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movimentation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val value: Double,
    val description: String,
    val tag: Tag = Tag.UNKNOWN,
    val spendAt: Long
)


