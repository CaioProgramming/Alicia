package com.ilustris.alicia.features.finnance.domain.mapper

import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.domain.data.MovimentationInfo

class MovimentationMapper {

    fun mapMovimentations(movimentations: List<Movimentation>): List<MovimentationInfo> {
        val movimentationInfos = ArrayList<MovimentationInfo>()
        val movimentationGroups = movimentations.groupBy { it.tag }

        movimentationGroups.forEach { t, u ->
            movimentationInfos.add(MovimentationInfo(t, u))
        }

        return movimentationInfos
    }

}