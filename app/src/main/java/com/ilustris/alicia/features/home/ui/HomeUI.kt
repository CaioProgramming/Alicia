@file:OptIn(ExperimentalMaterialApi::class)

package com.ilustris.alicia.features.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.airbnb.lottie.compose.*
import com.ilustris.alicia.R
import com.ilustris.alicia.features.home.presentation.HomeAction
import com.ilustris.alicia.features.home.presentation.HomeViewModel
import com.ilustris.alicia.features.home.ui.components.SheetInput
import com.ilustris.alicia.features.home.ui.components.TopBar
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.features.messages.ui.MessagesList
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.ui.theme.toolbarColor
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun HomeUI(title: String) {


    val viewModel: HomeViewModel = hiltViewModel()
    val messages = viewModel.messages.collectAsState(initial = emptyList())
    val showInput = viewModel.showInput.observeAsState(initial = false)
    val profitList = viewModel.profit.collectAsState(initial = emptyList())
    val lossList = viewModel.loss.collectAsState(initial = emptyList())
    val amount = viewModel.amount.collectAsState(initial = 0.00)
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
                    when (action) {
                        Action.NAME -> viewModel.launchAction(HomeAction.SaveUser(value))
                        Action.PROFIT -> viewModel.launchAction(
                            HomeAction.SaveProfit(
                                description,
                                value,
                                tag
                            )
                        )
                        Action.LOSS -> viewModel.launchAction(
                            HomeAction.SaveLoss(
                                description,
                                value,
                                tag
                            )
                        )
                        Action.GOAL -> viewModel.launchAction(
                            HomeAction.SaveGoal(
                                description,
                                value,
                                tag
                            )
                        )
                        Action.HISTORY, Action.BALANCE, Action.LOSS_HISTORY, Action.PROFIT_HISTORY, Action.GOAL_HISTORY -> viewModel.launchAction(
                            HomeAction.GetHistory
                        )
                    }
                })
        }) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = toolbarColor(isSystemInDarkTheme()))
        ) {

            val (toolbar, messageList, suggestions, divider, animation) = createRefs()

            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.cute_cat)
            )
            val progress by animateLottieCompositionAsState(
                composition,
                isPlaying = true,
                iterations = LottieConstants.IterateForever
            )

            TopBar(title = title, icon = R.drawable.pretty_girl, onClickNavigation = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(toolbar) { top.linkTo(parent.top) }
            )



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
            }



            MessagesList(
                modifier = Modifier
                    .constrainAs(messageList) {
                        bottom.linkTo(suggestions.top)
                        top.linkTo(toolbar.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
                messages,
                profitList.value,
                lossList.value,
                amount.value
            ) { suggestion, value ->
                scope.launch {
                    when (suggestion.action) {
                        Action.NAME -> {
                            value?.let {
                                viewModel.launchAction(HomeAction.SaveUser(value))
                            }
                        }
                        Action.BALANCE, Action.HISTORY, Action.PROFIT_HISTORY, Action.LOSS_HISTORY -> viewModel.launchAction(
                            HomeAction.GetHistory
                        )
                        else -> {
                            sheetTitle = suggestion.action.description
                            sheetAction = suggestion.action
                            sheetPlaceHolder = getPlaceHolderMessage(suggestion.action)
                            bottomSheetState.show()
                        }
                    }

                }
            }


            chatInput(modifier = Modifier
                .constrainAs(suggestions) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.matchParent
                    height = if (showInput.value) Dimension.wrapContent else Dimension.value(0.dp)
                }, onDone = {
                keyboardController?.hide()
                viewModel.launchAction(HomeAction.SaveUser(it))
            })
        }
    }


}

@Composable
fun chatInput(modifier: Modifier, onDone: (String) -> Unit) {
    var message by remember {
        mutableStateOf("")
    }

    TextField(
        value = message,
        onValueChange = { message = it },
        textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W400),
        placeholder = {
            androidx.compose.material3.Text(
                style = MaterialTheme.typography.bodyMedium,
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
            onDone(message)
            message = ""
        }),
        trailingIcon = {
            IconButton(
                onClick = {
                    onDone(message)
                    message = ""
                },
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_send_24),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
                    contentDescription = "enviar",
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.onBackground,
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
        HomeUI(title = "Alicia app")
    }
}
