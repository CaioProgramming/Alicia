@file:OptIn(ExperimentalMaterialApi::class)

package com.ilustris.alicia.features.messages.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ilustris.alicia.R
import com.ilustris.alicia.core.theme.AliciaTheme
import com.ilustris.alicia.core.theme.aliciaBrush
import com.ilustris.alicia.core.theme.aliciaColors
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Sender
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.features.messages.domain.model.MessageGroup
import com.ilustris.alicia.features.messages.presentation.ChatAction
import com.ilustris.alicia.features.messages.presentation.ChatState
import com.ilustris.alicia.features.messages.presentation.ChatViewModel
import com.ilustris.alicia.utils.gradientAnimation
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

@ExperimentalComposeUiApi
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val messages = viewModel.messages.collectAsStateWithLifecycle().value
    val suggestions = viewModel.suggestions.collectAsStateWithLifecycle().value
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val generalBrush =
        if (state is ChatState.Loading) {
            gradientAnimation(aliciaColors(), duration = 5.seconds)
        } else {
            aliciaBrush()
        }

    ChatView(
        appMessages = messages,
        suggestions = suggestions,
        navController = navController,
        state = state,
        loadingBrush = generalBrush,
    ) {
        viewModel.launchAction(ChatAction.SendMessage(it))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatView(
    appMessages: List<MessageGroup> = emptyList(),
    suggestions: List<String> = emptyList(),
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    state: ChatState?,
    loadingBrush: Brush,
    onSendMessage: (String) -> Unit,
) {
    val listState = rememberLazyListState()

    val isExpanded by remember {
        derivedStateOf {
            if (appMessages.isEmpty()) {
                true
            } else {
                listState.layoutInfo
                    .visibleItemsInfo
                    .any { it.key == "collapse_toolbar" }
            }
        }
    }

    val topBarAlpha =
        animateFloatAsState(
            if (isExpanded) 0f else 1f,
            tween(2.seconds.toInt(DurationUnit.MILLISECONDS), easing = EaseIn),
        )

    val scaffoldState = rememberScaffoldState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(appMessages) {
        if (appMessages.isNotEmpty()) {
            listState.scrollToItem(appMessages.size - 1)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background.copy(topBarAlpha.value),
                    ),
                navigationIcon = {
                    IconButton(
                        {
                            navController.popBackStack()
                        },
                        colors =
                            IconButtonDefaults.filledIconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onBackground,
                                containerColor = Color.Transparent,
                            ),
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
                title = {
                    AnimatedVisibility(
                        !isExpanded,
                        enter = slideInVertically(),
                        exit = fadeOut(),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Column(verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxSize()) {
                            Text(
                                stringResource(R.string.app_name),
                                modifier = Modifier.padding(12.dp).fillMaxWidth().wrapContentHeight(),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(COLLAPSED_TOP_BAR_HEIGHT),
            )
        },
        bottomBar = {
            ChatInput(
                suggestions,
                onDone = onSendMessage,
                state = state,
                brush = loadingBrush,
                modifier = Modifier.padding(16.dp).wrapContentHeight(),
            )
        },
        modifier = modifier.fillMaxSize(),
    ) {
        MessagesList(
            messages = appMessages,
            listState = listState,
            modifier = Modifier.padding(it).fillMaxSize(),
            brush = loadingBrush,
        ) { }
    }
}

@Composable
fun ChatInput(
    suggestions: List<String> = emptyList(),
    state: ChatState?,
    modifier: Modifier,
    brush: Brush,
    onDone: (String) -> Unit,
) {
    var message by remember {
        mutableStateOf("")
    }

    val isLoading = state == ChatState.Loading

    val backgroundColor =
        animateColorAsState(
            if (state is ChatState.Error) {
                Color.Red.copy(alpha = .60f)
            } else {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = .20f)
            },
            tween(1500, easing = EaseIn),
        )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .wrapContentSize()
                .background(backgroundColor.value, RoundedCornerShape(25.dp))
                .clip(RoundedCornerShape(25.dp))
                .animateContentSize(),
    ) {
        AnimatedVisibility(suggestions.isNotEmpty() && state is ChatState.Idle, modifier = Modifier.wrapContentSize()) {
            LazyRow(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(25.dp)),
            ) {
                items(suggestions.size) { index ->
                    val suggestion = suggestions[index]
                    Box(
                        modifier =
                            Modifier
                                .padding(8.dp)
                                .clip(RoundedCornerShape(25.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .clickable {
                                    message = suggestion
                                },
                    ) {
                        Text(
                            text = suggestion,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(8.dp),
                        )
                    }
                }
            }
        }

        AnimatedVisibility(state is ChatState.Idle, modifier = Modifier.wrapContentSize()) {
            TextField(
                value = message,
                modifier = Modifier.fillMaxWidth(),
                colors =
                    TextFieldDefaults.colors(
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    ),
                onValueChange = {
                    if (it.length <= 300) {
                        message = it
                    }
                },
                textStyle = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W500),
                placeholder = {
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        text = "Envie uma mensagem para começar",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrect = true,
                    ),
                keyboardActions =
                    KeyboardActions(onDone = {
                        if (message.isNotEmpty()) {
                            onDone(message)
                            message = ""
                        }
                    }),
                trailingIcon = {
                    IconButton(
                        enabled = message.isNotEmpty(),
                        colors =
                            IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary,
                            ),
                        onClick = {
                            if (message.isNotEmpty()) {
                                onDone(message)
                                message = ""
                            }
                        },
                        modifier =
                            Modifier
                                .size(48.dp),
                    ) {
                        AnimatedVisibility(
                            visible = message.isNotEmpty(),
                            enter = scaleIn(),
                            exit = scaleOut(),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "enviar",
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                },
            )
        }

        AnimatedVisibility(isLoading, modifier = Modifier.padding(8.dp)) {
            // create a infinite rotation animation
            val infiniteTransition = rememberInfiniteTransition()
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec =
                    infiniteRepeatable(
                        animation = tween(durationMillis = 5000, easing = EaseIn),
                        repeatMode = RepeatMode.Reverse,
                    ),
                label = "loadingRotation",
            )

            Box(
                Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .rotate(rotation)
                    .background(brush, CircleShape),
            ) { }
        }

        AnimatedVisibility(state is ChatState.Error, modifier = Modifier.wrapContentSize().padding(16.dp)) {
            Text(
                (state as? ChatState.Error)?.message ?: stringResource(R.string.default_error),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

fun getPlaceHolderMessage(action: Action): String =
    when (action) {
        Action.PROFIT -> "Com o que você ganhou?"
        Action.LOSS -> "Com o que você gastou?"
        Action.GOAL -> "Qual o seu objetivo"
        Action.NAME -> "Como posso te chamar?"
        else -> ""
    }

@Suppress("ktlint:standard:function-naming")
@Composable
fun CollapseToolbar(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Image(
            painterResource(id = R.drawable.pretty_girl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(100.dp)
                    .clip(CircleShape),
        )

        Text(
            stringResource(R.string.app_name),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1080px,height=2424px,cutout=punch_hole",
    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000,
)

@Suppress("ktlint:standard:function-naming")
@Composable
fun DefaultPreview() {
    AliciaTheme {
        ChatView(
            state = ChatState.Error(null),
            loadingBrush = gradientAnimation(aliciaColors(), duration = 5.seconds),
            suggestions =
                Action
                    .values()
                    .map { getPlaceHolderMessage(it) }
                    .filter { it.isNotEmpty() },
            appMessages =
                List(3) {
                    MessageGroup(
                        "Messasges $it",
                        messages =
                            List(5) {
                                Message(
                                    id = Random.nextInt(),
                                    message = "Hello $it",
                                    type = null,
                                    sender = if (it % 2 == 0) Sender.USER else Sender.BOT,
                                )
                            },
                    )
                },
        ) {}
    }
}

val COLLAPSED_TOP_BAR_HEIGHT = 70.dp
val EXPANDED_TOP_BAR_HEIGHT = 170.dp
