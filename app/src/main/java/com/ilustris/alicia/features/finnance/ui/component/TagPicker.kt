package com.ilustris.alicia.features.finnance.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.home.ui.components.EmojiSheet

@Composable
fun TagPicker(tag: Tag, onSelect: (Tag) -> Unit) {

    var categoryVisible by remember {
        mutableStateOf(false)
    }



    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier =
                Modifier
                    .wrapContentWidth()
                    .background(tag.textColor, RoundedCornerShape(25.dp))
                    .padding(8.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .clickable {
                        categoryVisible = !categoryVisible
                    },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = tag.description,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.W500,
            )
            Image(
                painterResource(id = tag.icon),
                colorFilter = ColorFilter.tint(tag.textColor),
                contentDescription = null,
                modifier =
                    Modifier
                        .padding(start = 8.dp)
                        .size(24.dp)
                        .background(MaterialTheme.colorScheme.background, CircleShape)
                        .padding(4.dp),
            )
        }

        AnimatedVisibility(
            visible = categoryVisible,
            enter = expandVertically(tween(500)),
            exit = shrinkVertically(tween(500)),
        ) {
            EmojiSheet(onSelectTag = {
                onSelect(it)
                categoryVisible = false
            })
        }
    }

}