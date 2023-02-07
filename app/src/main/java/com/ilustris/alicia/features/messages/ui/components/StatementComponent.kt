package com.ilustris.alicia.features.messages.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.constraintlayout.compose.Visibility
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
import com.ilustris.alicia.utils.formatToCurrencyText
import java.util.*

@Composable
fun StatementComponent(
    movimentation: Movimentation,
    modifier: Modifier,
    clipText: Boolean = true,
    showTag: Boolean = false,
    textColor: Color = MaterialTheme.colorScheme.onSecondary
) {
    val visible by remember {
        mutableStateOf(false).apply {
            this.value = true
        }
    }
    val formattedDate = Calendar.getInstance().apply {
        timeInMillis = movimentation.spendAt
    }.time.format(DateFormats.DD_MM_YYY)

    val showTag by remember {
        mutableStateOf(showTag)
    }

    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(animationSpec = tween(1500)),
        exit = shrinkVertically(animationSpec = tween(1500))
    ) {

    }
    ConstraintLayout(modifier = modifier.padding(8.dp)) {
        val (tag, descriptionText, movimentationDetails) = createRefs()


        Text(text = movimentation.tag.emoji, modifier = Modifier
            .constrainAs(tag) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                height = Dimension.wrapContent
                visibility = if (showTag) Visibility.Visible else Visibility.Gone
            }
            .padding(8.dp))

        Text(
            text = movimentation.description,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.W800,
            textAlign = TextAlign.Start,
            color = textColor,
            maxLines = 2,
            modifier = Modifier
                .constrainAs(descriptionText) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(tag.end)
                    end.linkTo(movimentationDetails.start)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                }
                .padding(horizontal = 8.dp)
        )
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.constrainAs(movimentationDetails) {
                top.linkTo(descriptionText.bottom)
                bottom.linkTo(descriptionText.bottom)
                end.linkTo(parent.end)
                height = Dimension.wrapContent
            }) {
            Text(
                text = movimentation.value.formatToCurrencyText(),
                fontWeight = FontWeight.W500,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.labelMedium,
                color = textColor
            )
            Text(
                text = formattedDate,
                fontWeight = FontWeight.W300,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.labelSmall,
                color = textColor.copy(alpha = 0.5f)
            )
        }
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
        ), Modifier.fillMaxWidth(), showTag = false
    )
}