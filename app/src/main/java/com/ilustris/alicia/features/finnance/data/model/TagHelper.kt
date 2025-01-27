package com.ilustris.alicia.features.finnance.data.model

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.R
import com.ilustris.alicia.core.theme.HexagonShape
import com.ilustris.alicia.features.finnance.ui.component.BillsBadge
import com.ilustris.alicia.features.finnance.ui.component.DefaultBadge
import com.ilustris.alicia.features.finnance.ui.component.EducationBadge
import com.ilustris.alicia.features.finnance.ui.component.FoodBadge
import com.ilustris.alicia.features.finnance.ui.component.GameBadge
import com.ilustris.alicia.features.finnance.ui.component.HealthBadge
import com.ilustris.alicia.features.finnance.ui.component.PartyBadge
import com.ilustris.alicia.features.finnance.ui.component.PetBadge
import com.ilustris.alicia.features.finnance.ui.component.ShieldBadge
import com.ilustris.alicia.features.finnance.ui.component.ShoppingBadge
import com.ilustris.alicia.features.finnance.ui.component.TransportBadge
import com.ilustris.alicia.features.finnance.ui.component.TravelBadge
import kotlin.random.Random

object TagHelper {
    fun findBadgeResource(
        index: Int,
        tag: Tag,
    ): Int =
        try {
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
            tag.badges().first()
        }

    fun getRandomBadgeForTag(tag: Tag) = Random.nextInt(0, tag.badges().size)

    fun getRandomBadge() = Random.nextInt(0, commonBadges.size)

    fun tagShape(tag: Tag): Shape =
        when (tag) {
            Tag.GAMES -> HexagonShape()

            Tag.WORK ->
                RoundedCornerShape(
                    topStart = 10.dp,
                    topEnd = 10.dp,
                    bottomStart = 50.dp,
                    bottomEnd = 50.dp,
                )
            else -> CircleShape
        }

    val commonBadges =
        listOf(R.drawable.common_badge_1, R.drawable.common_badge_2, R.drawable.common_badge_3)
    val transportBadges =
        listOf(R.drawable.car_badge_1, R.drawable.car_badge_2, R.drawable.car_badge_3)
    val gameBadges =
        listOf(R.drawable.game_badge_1, R.drawable.game_badge_2, R.drawable.game_badge_3)
    val petBadges =
        listOf(R.drawable.pet_badge_1, R.drawable.pet_badge_2, R.drawable.pet_badge_3)
    val shoppingBadges =
        listOf(
            R.drawable.shopping_badge_1,
            R.drawable.shopping_badge_2,
            R.drawable.shopping_badge_3,
        )
    val partyBadges =
        listOf(R.drawable.party_badge_1, R.drawable.party_badge_2, R.drawable.party_badge_3)
    val travelBadges =
        listOf(R.drawable.travel_badge_1, R.drawable.travel_badge_2, R.drawable.travel_badge_3)
    val educationBadges =
        listOf(
            R.drawable.education_badge_1,
            R.drawable.education_badge_2,
            R.drawable.education_badge_3,
        )
    val workBadges =
        listOf(R.drawable.work_badge_1, R.drawable.work_badge_2, R.drawable.work_badge_3)
    val healthBadges =
        listOf(R.drawable.health_badge_1, R.drawable.health_badge_2, R.drawable.health_badge_3)
    val groeceryBadges =
        listOf(R.drawable.food_badge_1, R.drawable.food_badge_2, R.drawable.food_badge_3)

    val billsBadges =
        listOf(R.drawable.bill_badge_1, R.drawable.bill_badge_2, R.drawable.bill_badge_3)
}

@Composable
fun BadgeForTag(
    goal: Goal,
    showText: Boolean,
    isAnimated: Boolean,
    modifier: Modifier,
) {
    val tag = remember { goal.tag.findTag() }
    when (tag) {
        Tag.WORK ->
            ShieldBadge(
                goal,
                showText,
                isAnimated,
                modifier,
            )
        Tag.PETS ->
            PetBadge(
                goal,
                showText,
                isAnimated,
                modifier,
            )
        Tag.GROCERIES ->
            FoodBadge(
                goal,
                showText,
                isAnimated,
                modifier,
            )
        Tag.HEALTH ->
            HealthBadge(
                goal,
                showText,
                isAnimated,
                modifier,
            )
        Tag.BILLS ->
            BillsBadge(
                goal,
                showText,
                isAnimated,
                modifier,
            )
        Tag.TRANSPORT ->
            TransportBadge(
                goal,
                showText,
                isAnimated,
                modifier,
            )
        Tag.EDUCATION ->
            EducationBadge(
                goal,
                showText,
                isAnimated,
                modifier,
            )
        Tag.ENTERTAINMENT ->
            PartyBadge(
                goal,
                showText,
                isAnimated,
                modifier,
            )
        Tag.TRAVEL ->
            TravelBadge(
                goal,
                showText,
                isAnimated,
                modifier,
            )
        Tag.SHOPPING ->
            ShoppingBadge(
                goal,
                showText,
                isAnimated,
                modifier,
            )
        Tag.GAMES ->
            GameBadge(
                goal,
                showText,
                isAnimated,
                modifier,
            )
        else ->
            DefaultBadge(
                goal,
                showText,
                isAnimated,
                modifier,
            )
    }
}
