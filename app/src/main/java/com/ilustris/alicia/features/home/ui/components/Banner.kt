package com.ilustris.alicia.features.home.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.R
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.utils.formatToCurrencyText
import java.util.*

@Composable
fun Banner(goal: Goal, closeBanner: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                MaterialTheme.colors.primary,
                shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)
            )
    ) {
        IconButton(onClick = { closeBanner.invoke() }) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.round_close_24),
                colorFilter = ColorFilter.tint(color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary),
                contentDescription = "Fechar",
            )
        }
        Text(
            text = "Você alcançou sua meta ${goal.description} de ${goal.value.formatToCurrencyText()} 🥳",
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colors.onPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BannerPreview() {
    AliciaTheme {
        Banner(
            goal = Goal(
                description = "Nike air",
                value = 500.00,
                createdAt = Calendar.getInstance().timeInMillis,
                tag = Tag.CLOTHES
            )
        ) {

        }
    }
}