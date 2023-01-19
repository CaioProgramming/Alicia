package com.ilustris.alicia.features.home.presentation


import androidx.lifecycle.ViewModel
import com.ilustris.alicia.features.user.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
public class HomeViewModel @Inject public constructor(val userUseCase: UserUseCase) : ViewModel() {





}