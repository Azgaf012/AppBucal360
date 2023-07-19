package com.dapm.appbucal360.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapm.appbucal360.domain.user.LogoutUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuAdminViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}