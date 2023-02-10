package com.ilustris.alicia.features.finnance.domain.mapper

import com.himanshoe.charty.circle.model.CircleData
import com.himanshoe.charty.line.model.LineData
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.domain.data.GoalInfo
import com.ilustris.alicia.features.finnance.domain.data.MovimentationInfo
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
import java.util.*

class MovimentationMapper {

    fun mapMovimentations(movimentations: List<Movimentation>): List<MovimentationInfo> {
        val movimentationInfos = ArrayList<MovimentationInfo>()
        val movimentationGroups = movimentations.groupBy { it.tag }

        movimentationGroups.forEach { tag, movimentations ->
            movimentationInfos.add(MovimentationInfo(tag, movimentations = movimentations))
        }

        return movimentationInfos
    }

    fun mapMovimentationsByDay(movimentations: List<Movimentation>): List<MovimentationInfo> {
        val movimentationInfos = ArrayList<MovimentationInfo>()
        val movimentationGroups = movimentations.sortedByDescending { it.spendAt }.groupBy {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = it.spendAt
            }
            calendar[Calendar.DAY_OF_YEAR]
        }

        movimentationGroups.forEach { day, movimentation ->
            movimentationInfos.add(
                MovimentationInfo(
                    tag = Tag.UNKNOWN,
                    movimentations = movimentation
                )
            )
        }
        return movimentationInfos
    }

    fun mapMovimentationsToLineData(movimentations: List<Movimentation>): List<LineData> {
        val movimentationsGroup = mapMovimentationsByDay(movimentations).reversed().map {
            val firstMovimentation = it.movimentations.first()
            val calendar = Calendar.getInstance().apply {
                timeInMillis = firstMovimentation.spendAt
            }
            val lastAmmount =
                movimentations.filter { it.spendAt <= calendar.timeInMillis }.sumOf { it.value }
            LineData(
                xValue = calendar.time.format(DateFormats.DD_MM),
                yValue = lastAmmount.toFloat()
            )
        }


        return movimentationsGroup
    }

    fun mapMovimentationsToCircleData(movimentations: List<Movimentation>) =
        mapMovimentations(movimentations).map { info ->
            val groupSum = info.movimentations.sumOf { it.value }
            val yValue = if (groupSum < 0) groupSum * -1 else groupSum
            CircleData(
                color = info.tag.color,
                xValue = info.tag.description,
                yValue = yValue.toFloat()
            )
        }.sortedBy { it.yValue }

    fun mapGoalsToInfo(goals: List<Goal>) = goals.groupBy { it.tag }.map {
        GoalInfo(
            "${it.key.emoji} ${it.key.description}",
            "Concluiu ${it.value.filter { it.isComplete }.size} de ${it.value.size}",
            it.value.sortedByDescending { it.isComplete })
    }


}