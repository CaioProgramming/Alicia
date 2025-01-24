package com.ilustris.alicia.features.messages.data.model

import com.google.gson.annotations.SerializedName
import com.ilustris.alicia.utils.emptyString

open class MetaData(
    @SerializedName("tag")
    val type: String? = null,
)

data class UserMetaData(
    val name: String = emptyString(),
) : MetaData()

data class MovimentationMetaData(
    val value: Double = 0.0,
    val description: String = emptyString(),
    val tag: String = emptyString(),
) : MetaData()

data class GoalMetaData(
    val value: Double = 0.0,
    val description: String = emptyString(),
    val tag: String = emptyString(),
) : MetaData()