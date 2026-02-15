package com.example.kinomaptest.ui.screen.login

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data class Error(val message: String?) : LoginUiState()
}
