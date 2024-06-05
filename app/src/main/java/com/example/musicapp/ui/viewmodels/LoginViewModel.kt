package com.example.musicapp.ui.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginForm(
    val email: String,
    val password: String
)

sealed class LoginUiState {
    data object Loading : LoginUiState()
    data object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel : ViewModel() {
    private var _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    var loginForm by mutableStateOf(LoginForm("blackgamerofficial123@gmail.com", "123456789"))
        private set

//    init {
//        login()
//    }

    fun setEmail(email: String) {
        loginForm = loginForm.copy(email = email)
    }

    fun setPassword(password: String) {
        loginForm = loginForm.copy(password = password)
    }

    fun login(
        context: Context?=null
    ){
        _loginUiState.value = LoginUiState.Loading
        Firebase.auth.signInWithEmailAndPassword(loginForm.email, loginForm.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginUiState.value = LoginUiState.Success
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                } else {
                    _loginUiState.value = LoginUiState.Error(task.exception?.message ?: "An error occurred")
                    Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                }
                Firebase.appCheck.installAppCheckProviderFactory(
                    PlayIntegrityAppCheckProviderFactory.getInstance()
                )
            }
    }
}