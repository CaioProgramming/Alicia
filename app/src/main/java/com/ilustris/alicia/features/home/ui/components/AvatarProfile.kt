package com.ilustris.alicia.features.home.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.CHAT_SCREEN
import com.ilustris.alicia.R
import com.ilustris.alicia.features.home.domain.model.Avatar
import com.ilustris.alicia.ui.theme.AliciaTheme

@Composable
fun AvatarProfile(avatar: Avatar, enabled: Boolean = true, onSelectChat: (String) -> Unit) {
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(id = avatar.icon),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(8.dp)
                .size(75.dp)
                .background(color = avatar.backColor, CircleShape)
                .padding(4.dp)
                .clip(CircleShape)
                .clickable(enabled = enabled) {
                    onSelectChat(avatar.redirect)
                }
        )

        Text(
            text = avatar.name,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.W500
            ),
            textAlign = TextAlign.Center
        )
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AvatarPreview() {
    AliciaTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            AvatarProfile(
                Avatar(
                    stringResource(id = R.string.app_name),
                    R.drawable.pretty_girl,
                    MaterialTheme.colorScheme.primary,
                    CHAT_SCREEN
                ),
                true
            ) {

            }
        }

    }
}