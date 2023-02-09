package com.ilustris.alicia.features.home.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.ui.theme.aliciaBrush
import com.ilustris.alicia.utils.formatToCurrencyText
import java.util.*

@Composable
fun Banner(goal: Goal, visible: Boolean = false, modifier: Modifier, closeBanner: () -> Unit) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(animationSpec = tween(1000)),
        exit = slideOutVertically(animationSpec = tween(1500))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .clickable {
                    closeBanner()
                }
                .background(
                    aliciaBrush(),
                    shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)
                )
        ) {

            Text(
                text = "Você alcançou sua meta ${goal.description} de ${goal.value.formatToCurrencyText()} 🥳",
                modifier = Modifier.padding(32.dp),
                color = MaterialTheme.colors.onPrimary,
                textAlign = TextAlign.Center
            )
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BannerPreview() {
    AliciaTheme {
        Banner(
            visible = true,
            modifier = Modifier.fillMaxSize(),
            goal = Goal(
                description = "Nike air",
                value = 500.00,
                createdAt = Calendar.getInstance().timeInMillis,
                tag = Tag.SHOPPING
            )
        ) {

        }
    }
}