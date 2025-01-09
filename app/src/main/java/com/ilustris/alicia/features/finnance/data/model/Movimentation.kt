package com.ilustris.alicia.features.finnance.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movimentation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var value: Double,
    val description: String?,
    val tag: String? = Tag.UNKNOWN.name,
    val spendAt: Long,
) {
    fun promptDescription() = "$description with value $value"
}
