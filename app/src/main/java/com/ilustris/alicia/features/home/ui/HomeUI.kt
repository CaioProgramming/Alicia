package com.ilustris.alicia.features.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import com.ilustris.alicia.R
import com.ilustris.alicia.features.home.presentation.HomeAction
import com.ilustris.alicia.features.home.presentation.HomeViewModel
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.ui.MessagesList
import com.ilustris.alicia.features.home.ui.components.SheetInput
import com.ilustris.alicia.features.home.ui.components.TopBar
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.features.messages.ui.MessageSuggestionsList
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.ui.theme.toolbarColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeUI(title: String) {


    val viewModel: HomeViewModel = hiltViewModel()
    val messages = viewModel.messages.collectAsState(initial = emptyList())
    val suggestionsList = viewModel.suggestions.collectAsState(initial = emptyList())
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
        sheetShape = RoundedCornerShape(10.dp),
        sheetContent = {
            SheetInput(
                title = sheetTitle,
                action = sheetAction,
                placeHolder = sheetPlaceHolder,
                focusRequester = focusRequester,
                onConfirmClick = { description, value, action ->
                    scope.launch {
                        bottomSheetState.hide()
                        focusRequester.freeFocus()
                    }
                    when (action) {
                        Action.NAME -> viewModel.launchAction(HomeAction.SaveUser(value))
                        Action.PROFIT -> viewModel.launchAction(
                            HomeAction.SaveProfit(
                                description,
                                value
                            )
                        )
                        Action.LOSS -> viewModel.launchAction(
                            HomeAction.SaveLoss(
                                description,
                                value
                            )
                        )
                        Action.GOAL -> viewModel.launchAction(
                            HomeAction.SaveGoal(
                                description,
                                value
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
                    .background(color = toolbarColor(isSystemInDarkTheme()))
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



            MessagesList(modifier = Modifier
                .constrainAs(messageList) {
                    bottom.linkTo(suggestions.top)
                    top.linkTo(toolbar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .padding(horizontal = 16.dp, vertical = 8.dp),
                messages,
                profitList.value,
                lossList.value,
                amount.value)


            Divider(modifier = Modifier.constrainAs(divider) {
                bottom.linkTo(suggestions.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.matchParent
                height = Dimension.value(2.dp)
                visibility = Visibility.Gone
            }, color = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f))

            MessageSuggestionsList(
                modifier = Modifier
                    .constrainAs(suggestions) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.matchParent
                        height = Dimension.preferredWrapContent
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                suggestions = suggestionsList,
                onSelectSuggestion = { suggestion, value ->
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
                                sheetTitle = suggestion.name
                                sheetAction = suggestion.action
                                sheetPlaceHolder = getPlaceHolderMessage(suggestion.action)
                                bottomSheetState.show()
                            }
                        }

                    }
                }
            )

        }
    }


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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AliciaTheme {
        HomeUI(title = "Alicia app")
    }
}
