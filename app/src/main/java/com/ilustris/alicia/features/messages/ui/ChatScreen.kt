@file:OptIn(ExperimentalMaterialApi::class)

package com.ilustris.alicia.features.messages.ui

import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.*
import com.ilustris.alicia.R
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.home.ui.components.Banner
import com.ilustris.alicia.features.home.ui.components.SheetInput
import com.ilustris.alicia.features.home.ui.components.TopBar
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.features.messages.presentation.ChatAction
import com.ilustris.alicia.features.messages.presentation.ChatViewModel
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.ui.theme.toolbarColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun ChatScreen(title: String, navController: NavHostController) {


    val viewModel: ChatViewModel = hiltViewModel()
    val messages = viewModel.messages.collectAsState(initial = emptyList())
    val showInput = viewModel.showInput.collectAsState(initial = null)
    val playNewMessage = viewModel.playNewMessage.observeAsState(initial = false)
    val profitList = viewModel.profit.collectAsState(initial = emptyList())
    val lossList = viewModel.loss.collectAsState(initial = emptyList())
    val goals = viewModel.goals.collectAsState(initial = emptyList())
    val amount = viewModel.amount.collectAsState(initial = 0.00)
    var completedGoal: Goal? = null

    var bannerVisible by remember {
        mutableStateOf(false)
    }

    var sheetPlaceHolder by remember {
        mutableStateOf("O que você comprou?")
    }

    var sheetTitle by remember {
        mutableStateOf("Comprinha do mês")
    }

    var sheetAction by remember {
        mutableStateOf(Action.NAME)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val scope = rememberCoroutineScope()

    val focusRequester = remember { FocusRequester() }


    if (bottomSheetState.isVisible) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
    val mediaPlayer = MediaPlayer.create(LocalContext.current, R.raw.bell)
    val aliciaPlayer = MediaPlayer.create(LocalContext.current, R.raw.pop)

    if (playNewMessage.value == true) aliciaPlayer.start()

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(15.dp),

        sheetContent = {
            SheetInput(
                title = sheetTitle,
                action = sheetAction,
                placeHolder = sheetPlaceHolder,
                focusRequester = focusRequester,
                onConfirmClick = { description, value, tag, action ->
                    scope.launch {
                        bottomSheetState.hide()
                        focusRequester.freeFocus()
                    }
                    if (!mediaPlayer.isPlaying) {
                        mediaPlayer.start()
                    }
                    when (action) {
                        Action.NAME -> viewModel.launchAction(ChatAction.SaveUser(value))
                        Action.PROFIT -> viewModel.launchAction(
                            ChatAction.SaveProfit(
                                description,
                                value,
                                tag
                            )
                        )
                        Action.LOSS -> viewModel.launchAction(
                            ChatAction.SaveLoss(
                                description,
                                value,
                                tag
                            )
                        )
                        Action.GOAL -> viewModel.launchAction(
                            ChatAction.SaveGoal(
                                description,
                                value,
                                tag
                            )
                        )
                        Action.HISTORY, Action.BALANCE, Action.GOAL_HISTORY -> viewModel.launchAction(
                            ChatAction.GetHistory
                        )
                    }
                })
        }) {
        val celebrateComposition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.celebrate)
        )
        var isCelebrationPlaying by remember {
            mutableStateOf(false)
        }

        val celebrateProgress by animateLottieCompositionAsState(
            celebrateComposition,
            isPlaying = isCelebrationPlaying,
        )
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = toolbarColor(isSystemInDarkTheme()))
        ) {

            val (toolbar, messageList, suggestions, banner, animation) = createRefs()

            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.cute_cat)
            )
            val progress by animateLottieCompositionAsState(
                composition,
                isPlaying = true,
                iterations = LottieConstants.IterateForever
            )



            TopBar(title = title, icon = R.drawable.pretty_girl, onClickNavigation = {
                if (goals.value.isNotEmpty() || amount.value != 0.0) {
                    navController.popBackStack()
                }
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(toolbar) { top.linkTo(parent.top) }
            )


            completedGoal?.let {
                Banner(goal = it, bannerVisible, modifier = Modifier.constrainAs(banner) {
                    top.linkTo(toolbar.top)
                    bottom.linkTo(toolbar.bottom)
                    width = Dimension.matchParent
                    height = Dimension.matchParent
                }) {
                    bannerVisible = false
                }
            }


            if (messages.value.isEmpty()) {
                LottieAnimation(
                    composition = composition,
                    progress,
                    Modifier.constrainAs(animation) {

                        top.linkTo(toolbar.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.value(200.dp)
                        height = Dimension.value(200.dp)

                    })
            } else {
                MessagesList(
                    modifier = Modifier
                        .constrainAs(messageList) {
                            if (showInput.value == null) bottom.linkTo(suggestions.top) else bottom.linkTo(
                                parent.bottom
                            )
                            top.linkTo(toolbar.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                    messages,
                    profitList.value,
                    lossList.value,
                    goals.value,
                    amount.value,
                    onSelectSuggestion = { suggestion, value ->
                        scope.launch {
                            when (suggestion.action) {
                                Action.NAME -> {
                                    value?.let {
                                        viewModel.launchAction(ChatAction.SaveUser(value))
                                    }
                                }
                                Action.BALANCE, Action.HISTORY -> viewModel.launchAction(
                                    ChatAction.GetHistory
                                )
                                Action.GOAL_HISTORY -> viewModel.launchAction(ChatAction.GetGoals)
                                else -> {
                                    sheetTitle = suggestion.action.description
                                    sheetAction = suggestion.action
                                    sheetPlaceHolder = getPlaceHolderMessage(suggestion.action)
                                    bottomSheetState.show()
                                }
                            }

                        }
                    },
                    openStatement = {
                        navController.navigate("statement")
                    },
                    openGoal = {
                        navController.navigate("goals")
                    }
                )
            }



            if (goals.value.isNotEmpty()) {
                val goalPlayer = MediaPlayer.create(LocalContext.current, R.raw.celebrate_audio)
                LaunchedEffect(goals) {
                    val completedGoals =
                        goals.value.filter { it.value <= amount.value && !it.isComplete }
                    if (completedGoals.isNotEmpty()) {
                        isCelebrationPlaying = true
                        if (!goalPlayer.isPlaying) {
                            goalPlayer.start()
                        }
                        if (isCelebrationPlaying && progress == 1f) {
                            isCelebrationPlaying = false
                        }
                        completedGoals.forEach {
                            viewModel.launchAction(ChatAction.CompleteGoal(it))
                        }
                        completedGoal = completedGoals.last()
                        bannerVisible = true
                        delay(3000L)
                        bannerVisible = false

                    }
                }
            }

            AnimatedVisibility(visible = showInput.value == null,
                enter = slideInVertically(),
                exit = fadeOut(),
                modifier = Modifier
                    .constrainAs(suggestions) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.matchParent
                    }
                    .padding(horizontal = 16.dp, vertical = 4.dp)) {
                ChatInput(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), onDone = {
                    keyboardController?.hide()
                    viewModel.launchAction(ChatAction.SaveUser(it))
                })
            }


        }
        LottieAnimation(
            celebrateComposition,
            celebrateProgress,
            modifier = Modifier.fillMaxSize()
        )
    }


}

@Composable
fun ChatInput(modifier: Modifier, onDone: (String) -> Unit) {
    var message by remember {
        mutableStateOf("")
    }

    TextField(
        value = message,
        onValueChange = {
            if (it.length <= 45) {
                message = it
            }
        },
        textStyle = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W500),
        placeholder = {
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = Action.NAME.description,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            capitalization = KeyboardCapitalization.Words,
            autoCorrect = false,
        ),
        keyboardActions = KeyboardActions(onDone = {
            if (message.isNotEmpty()) {
                onDone(message)
                message = ""
            }
        }),
        trailingIcon = {
            IconButton(
                onClick = {
                    if (message.isNotEmpty()) {
                        onDone(message)
                        message = ""
                    }
                },
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.secondary, shape = CircleShape)
                    .padding(4.dp)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(0.5f),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_send_24),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSecondary),
                    contentDescription = "enviar",
                    contentScale = ContentScale.Inside,
                    alignment = Alignment.Center
                )
            }

        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.onBackground,
            cursorColor = MaterialTheme.colorScheme.tertiary,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            disabledTextColor = Color.Transparent,
            focusedLabelColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .wrapContentHeight()
            .border(
                1.dp,
                MaterialTheme.colorScheme.onBackground.copy(0.5f),
                shape = RoundedCornerShape(25.dp)
            )

    )
}

fun getPlaceHolderMessage(action: Action): String {
    return when (action) {
        Action.PROFIT -> "Com o que você ganhou?"
        Action.LOSS -> "Com o que você gastou?"
        Action.GOAL -> "Qual o seu objetivo"
        Action.NAME -> "Como posso te chamar?"
        else -> ""
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AliciaTheme {
        val navController = rememberNavController()

        ChatScreen(title = "Alicia app", navController = navController)
    }
}
