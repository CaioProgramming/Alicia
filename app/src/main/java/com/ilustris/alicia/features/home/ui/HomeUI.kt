package com.ilustris.alicia.features.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import com.ilustris.alicia.R
import com.ilustris.alicia.features.home.presentation.HomeAction
import com.ilustris.alicia.features.home.presentation.HomeViewModel
import com.ilustris.alicia.features.messages.data.model.Action
import com.ilustris.alicia.features.messages.ui.MessagesList
import com.ilustris.alicia.features.home.ui.components.SheetInput
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.home.ui.components.TopBar
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

    var sheetPlaceHolder by remember {
        mutableStateOf("Valor gasto")
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



    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(10.dp),
        sheetContent = {
            SheetInput(
                title = sheetTitle,
                action = sheetAction,
                placeHolder = sheetPlaceHolder,
                onConfirmClick = { description, value, action ->
                    scope.launch {
                        bottomSheetState.hide()
                    }
                    when (action) {
                        Action.NAME -> viewModel.launchAction(HomeAction.SaveUser(value))
                        Action.GAIN -> viewModel.launchAction(HomeAction.SaveProfit(description, value))
                        Action.LOSS -> viewModel.launchAction(HomeAction.SaveLoss(description, value))
                        Action.GOAL -> viewModel.launchAction(HomeAction.SaveGoal(description, value))

                    }
                })
        }) {

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

            val (toolbar, messageList, suggestions, animation) = createRefs()

            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.cute_cat)
            )
            val progress by animateLottieCompositionAsState(
                composition,
                isPlaying = true,
                iterations = LottieConstants.IterateForever
            )

            TopBar(title = title, icon = R.drawable.pretty_girl,
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
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .padding(vertical = 8.dp), messages) {
                scope.launch {
                    when (it.action) {
                        Action.GAIN, Action.NAME, Action.GOAL, Action.LOSS -> {
                            sheetPlaceHolder = getPlaceHolderMessage(it.action)
                            sheetAction = it.action
                            bottomSheetState.show()
                        }

                        else -> {

                        }
                    }

                }
            }

            MessageSuggestionsList(
                modifier = Modifier.constrainAs(suggestions) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.matchParent
                    height = Dimension.preferredWrapContent
                }.padding(16.dp),
                suggestions = suggestionsList,
                onSelectSuggestion = { suggestion, value ->
                    scope.launch {
                        when(suggestion.action) {
                            Action.NAME -> {
                                value?.let {
                                    viewModel.launchAction(HomeAction.SaveUser(value))
                                }
                            }
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
        Action.NONE -> ""
        Action.GAIN -> "Valor ganho"
        Action.LOSS -> "Valor gasto"
        Action.GOAL -> "Valor da meta"
        Action.NAME -> "Qual o seu nome"
        Action.USER -> ""
        Action.HEADER -> ""
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AliciaTheme {
        HomeUI(title = "Alicia app")
    }
}
