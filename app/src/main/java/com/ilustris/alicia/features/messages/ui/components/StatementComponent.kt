package com.ilustris.alicia.features.messages.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.utils.formatToCurrencyText
import java.util.*

@Composable
fun StatementComponent(
    movimentation: Movimentation,
    textColor: Color = MaterialTheme.colorScheme.onSecondary
) {
    val visible by remember {
        mutableStateOf(false).apply {
            this.value = true
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(animationSpec = tween(1500)),
        exit = shrinkVertically(animationSpec = tween(1500))
    ) {

    }
    ConstraintLayout {
        val (tag, descriptionText, movimentationDetails, divider) = createRefs()

        Text(text = movimentation.tag.emoji, modifier = Modifier
            .constrainAs(tag) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                height = Dimension.wrapContent
            }
            .padding(16.dp))

        Column(modifier = Modifier
            .constrainAs(descriptionText) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(tag.end)
                end.linkTo(movimentationDetails.start)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            }
            .padding(horizontal = 8.dp)) {

            Text(
                text = movimentation.description,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.W800,
                textAlign = TextAlign.Start,
                color = textColor,
                maxLines = 2,
            )
            Text(
                text = movimentation.tag.description,
                style = MaterialTheme.typography.labelSmall,
                color = textColor.copy(alpha = 0.5f),
                fontWeight = FontWeight.W300
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .constrainAs(movimentationDetails) {
                    top.linkTo(descriptionText.top)
                    bottom.linkTo(descriptionText.bottom)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                }
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = movimentation.value.formatToCurrencyText(),
                fontWeight = FontWeight.W500,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.labelMedium,
                color = textColor
            )
        }

        Box(
            modifier = Modifier
                .constrainAs(divider) {
                    bottom.linkTo(parent.bottom)
                }
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))
        )
    }
}

@Preview(showBackground = false)
@Composable
fun defaultPreview() {
    StatementComponent(
        Movimentation(
            description = "Nike Air 92",
            value = 150.00,
            tag = Tag.BILLS,
            spendAt = Calendar.getInstance().time.time
        ),
    )
}