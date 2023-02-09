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
                Tag.GIFTS -> giftBadges[index]
                else -> commonBadges[index]
            }
        } catch (e: Exception) {
            commonBadges.first()
        }
    }

    fun getRandomBadge(tag: Tag) = Random.nextInt(commonBadges.size)


    val transportBadges = listOf(R.drawable.car_badge_1)
    val gameBadges = listOf(R.drawable.game_badge_1)
    val petBadges = listOf(R.drawable.pet_badge_1)
    val shoppingBadges = listOf(R.drawable.shopping_badge_1)
    val commonBadges = listOf(R.drawable.common_badge_1)
    val giftBadges = listOf(R.drawable.gift_badge_1)


}