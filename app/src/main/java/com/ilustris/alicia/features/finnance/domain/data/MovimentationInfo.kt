package com.ilustris.alicia.features.finnance.domain.data

import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.data.model.Tag

data class MovimentationInfo(val tag: Tag, val movimentations: List<Movimentation>)