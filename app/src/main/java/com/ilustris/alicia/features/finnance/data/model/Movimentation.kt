package com.ilustris.alicia.features.finnance.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
data class Movimentation(
    @PrimaryKey
    val id: Int,
    val value: Double,
    val description: String,
    val type: MovimentationType = MovimentationType.UNKNOWN
)


enum class MovimentationType {
    FOOD, CLOTHING, TECH, TRIP, UNKNOWN
}
