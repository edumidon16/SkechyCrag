package com.example.skechycrag.ui.login

sealed class UserDetailState {
    data object Loading: UserDetailState()
    data class Error(val error: Boolean): UserDetailState()
    data class Success(val work:Boolean): UserDetailState()


}