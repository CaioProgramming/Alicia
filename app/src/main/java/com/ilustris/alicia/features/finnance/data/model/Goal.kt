package com.ilustris.alicia.features.finnance.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
data class Goal(
    @PrimaryKey
    val id: Int,
    val value: Double,
    val name: String,
    val description: String
)
