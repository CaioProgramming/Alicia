package com.ilustris.alicia.features.finnance.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.home.domain.model.Avatar
import com.ilustris.alicia.features.home.ui.components.AvatarProfile
import com.ilustris.alicia.features.home.ui.getAvatars

@Composable
fun SheetInfo(title: String, description: String, avatar: Avatar, okClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        AvatarProfile(avatar = avatar, onSelectChat = {})
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.W700,
                color = MaterialTheme.colorScheme.onSurface
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            shape = RoundedCornerShape(15.dp),
            onClick = { okClick() }, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Ok")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InfoPreview() {
    val avatar = getAvatars().last()
    SheetInfo(
        title = "Apresentando ${avatar.name}",
        description = "A ${avatar.name} está aqui para te ajudar a acompanhar suas metas, você pode clicar no ícone dela e ter uma visão melhor de todas as suas metas.",
        avatar = getAvatars().last()
    ) {

    }
}