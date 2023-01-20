package com.ilustris.alicia.features.home.ui

import ai.atick.material.MaterialColor
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import com.ilustris.alicia.R
import com.ilustris.alicia.features.home.presentation.HomeAction
import com.ilustris.alicia.features.home.presentation.HomeViewModel
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.features.messages.ui.MessagesList
import com.ilustris.alicia.features.home.ui.components.SheetInput
import com.ilustris.alicia.features.messages.domain.model.Message
import com.ilustris.alicia.features.home.ui.components.TopBar
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.ui.theme.toolbarColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeUI(title: String) {


    val viewModel: HomeViewModel = hiltViewModel()
    val messages = viewModel.messages.observeAsState()

    viewModel.launchAction(HomeAction.FetchUser)

    var sheetPlaceHolder by remember {
        mutableStateOf("Valor gasto")
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
            SheetInput(action = sheetAction, placeHolder = sheetPlaceHolder, onConfirmClick = {})
        }) {

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

            val (toolbar, divider, messageList, animation) = createRefs()

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
                    .constrainAs(toolbar) {
                        top.linkTo(parent.top)
                    }
                    .background(color = toolbarColor(isSystemInDarkTheme()))
            )

            Box(
                modifier = Modifier
                    .background(color = MaterialColor.Gray500.copy(alpha = 0.5f))
                    .fillMaxWidth()
                    .height(1.dp)
                    .constrainAs(divider) { top.linkTo(toolbar.bottom) }
            )


            LottieAnimation(composition = composition, progress, Modifier.constrainAs(animation) {

                top.linkTo(toolbar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.value(200.dp)
                height = Dimension.value(200.dp)

            })


            MessagesList(modifier = Modifier.constrainAs(messageList) {
                bottom.linkTo(parent.bottom)
                top.linkTo(parent.top)
                start.linkTo(parent.start, margin = 10.dp)
                end.linkTo(parent.end, margin = 10.dp)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }, messages.value?.toList() ?: emptyList()) {
                scope.launch {
                    when (it.action) {
                        Action.GAIN, Action.NAME, Action.GOAL, Action.LOSS -> {
                            sheetPlaceHolder = getPlaceHolderMessage(it.action)
                            sheetAction = it.action
                            if (bottomSheetState.isVisible) {
                                bottomSheetState.hide()
                            } else bottomSheetState.show()
                        }

                        else -> {

                        }
                    }

                }
            }

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
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AliciaTheme {
        HomeUI(title = "Alicia app")
    }
}

object MockData {
    val messages = listOf(
        Message("Hi", Action.USER),
        Message("I'm Alicia"),
        Message("I'm here to help you in your finnancial life", Action.GAIN),
        Message(":)", Action.NAME),
        Message("Ok Help me", Action.USER)
    ).reversed()
    val suggestions = listOf("Ganhei R$500", "Gastei R$300", "Gastei R$200", "Gastei R$20,00")
}