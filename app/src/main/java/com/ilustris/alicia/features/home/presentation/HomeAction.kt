package com.ilustris.alicia.features.home.presentation

sealed class HomeAction {

    object FetchUser : HomeAction()
    data class SaveUser(val name: String) : HomeAction()


}
