package com.example.skechycrag.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skechycrag.domain.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsersUseCase:GetUsersUseCase
): ViewModel() {

    private val _userState = MutableStateFlow<UserDetailState>(UserDetailState.Loading)
    val userState: StateFlow<UserDetailState> = _userState


    fun checkUser(username:String, password:String){
        //Creamos una corrutina para poder llamar al use case, ya que es un suspend fun
        viewModelScope.launch {
           if(getUsersUseCase(username, password)){
               _userState.update { UserDetailState.Success(true) }
           }else{
               _userState.update { UserDetailState.Error(false) }
           }
        }
    }
}