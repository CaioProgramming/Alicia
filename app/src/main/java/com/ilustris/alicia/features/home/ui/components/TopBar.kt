package com.ilustris.alicia.features.home.ui.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Visibility
import com.ilustris.alicia.R
import com.ilustris.alicia.core.theme.AliciaTheme

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String,
    @DrawableRes icon: Int,
    onClickNavigation: () -> Unit,
) {
    ConstraintLayout(
        modifier,
    ) {
        val (profile, backButton) = createRefs()

        Column(
            modifier =
                Modifier.constrainAs(profile) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painterResource(id = icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .padding(16.dp),
            )

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 4.dp),
            )
        }

        IconButton(
            modifier =
                Modifier.constrainAs(backButton) {
                    start.linkTo(parent.start)
                    top.linkTo(profile.top)
                    visibility = Visibility.Visible
                },
            onClick = {
                onClickNavigation()
            },
            colors =
                IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
        ) {
            Icon(Icons.Rounded.KeyboardArrowLeft, contentDescription = null)
        }
    }
}

@Preview(
    name = "TopBar",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun PreviewTopBarr() {
    AliciaTheme {
        TopBar(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface),
            icon = R.drawable.pretty_girl,
            title = stringResource(id = R.string.app_name),
            onClickNavigation = {},
        )
    }
}
