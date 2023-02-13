package com.ilustris.alicia.features.finnance.data.model

import com.ilustris.alicia.R
import kotlin.random.Random

object TagHelper {

    fun findBadgeResource(index: Int, tag: Tag): Int {
        return try {
            when (tag) {
                Tag.PETS -> petBadges[index]
                Tag.TRANSPORT -> transportBadges[index]
                Tag.SHOPPING -> shoppingBadges[index]
                Tag.GAMES -> gameBadges[index]
                Tag.TRAVEL -> travelBadges[index]
                Tag.EDUCATION -> educationBadges[index]
                Tag.WORK -> workBadges[index]
                Tag.ENTERTAINMENT -> partyBadges[index]
                Tag.GROCERIES -> groeceryBadges[index]
                Tag.HEALTH -> healthBadges[index]
                Tag.BILLS -> billsBadges[index]
                else -> commonBadges[index]
            }
        } catch (e: Exception) {
            e.printStackTrace()
            commonBadges.first()
        }
    }

    fun getRandomBadge() = Random.nextInt(0, commonBadges.size)

    private val commonBadges =
        listOf(R.drawable.common_badge_1, R.drawable.common_badge_2, R.drawable.common_badge_3)
    private val transportBadges =
        listOf(R.drawable.car_badge_1, R.drawable.car_badge_2, R.drawable.car_badge_3)
    private val gameBadges =
        listOf(R.drawable.game_badge_1, R.drawable.game_badge_2, R.drawable.game_badge_3)
    private val petBadges =
        listOf(R.drawable.pet_badge_1, R.drawable.pet_badge_2, R.drawable.pet_badge_3)
    private val shoppingBadges = listOf(
        R.drawable.shopping_badge_1,
        R.drawable.shopping_badge_2,
        R.drawable.shopping_badge_3
    )
    private val partyBadges =
        listOf(R.drawable.party_badge_1, R.drawable.party_badge_2, R.drawable.party_badge_3)
    private val travelBadges =
        listOf(R.drawable.travel_badge_1, R.drawable.travel_badge_2, R.drawable.travel_badge_3)
    private val educationBadges = listOf(
        R.drawable.education_badge_1,
        R.drawable.education_badge_2,
        R.drawable.education_badge_3
    )
    private val workBadges =
        listOf(R.drawable.work_badge_1, R.drawable.work_badge_2, R.drawable.work_badge_3)
    private val healthBadges =
        listOf(R.drawable.health_badge_1, R.drawable.health_badge_2, R.drawable.health_badge_3)
    private val groeceryBadges =
        listOf(R.drawable.food_badge_1, R.drawable.food_badge_2, R.drawable.food_badge_3)

    private val billsBadges =
        listOf(R.drawable.bill_badge_1, R.drawable.bill_badge_2, R.drawable.bill_badge_3)

}