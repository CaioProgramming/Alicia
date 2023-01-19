package com.ilustris.alicia.features.home.presentation

import com.ilustris.alicia.features.user.data.User

sealed class HomeState {
    object UserNotFoundState : HomeState()
    data class UserRetrieved(val user: User): HomeState()
}
