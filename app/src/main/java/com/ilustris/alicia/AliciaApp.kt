package com.ilustris.alicia

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class AliciaApp : Application()

const val CHAT_SCREEN = "chat"
const val MAIN_SCREEN = "main"
const val STATEMENT_SCREEN = "statement"
const val GOAL_SCREEN = "goals"