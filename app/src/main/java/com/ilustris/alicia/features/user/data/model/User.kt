package com.ilustris.alicia.features.user.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    val name: String = "",
    var avatar: Int = 0,
)
