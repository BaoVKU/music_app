package com.example.musicapp.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RegisterForm(
    val email: String,
    val password: String,
    val confirmPassword: String
)

sealed class RegisterUiState {
    data object Loading : RegisterUiState()
    data object Success : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

class RegisterViewModel : ViewModel() {
    private var _registerUiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Loading)
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState.asStateFlow()

    var registerForm by mutableStateOf(RegisterForm("", "", ""))
        private set

    private fun validateCredentials(): Boolean {
        return registerForm.email.isNotEmpty() && registerForm.password.isNotEmpty() && registerForm.password == registerForm.confirmPassword
    }

    fun setEmail(email: String) {
        registerForm = registerForm.copy(email = email)
    }

    fun setPassword(password: String) {
        registerForm = registerForm.copy(password = password)
    }

    fun setConfirmPassword(confirmPassword: String) {
        registerForm = registerForm.copy(confirmPassword = confirmPassword)
    }

    fun register(context: Context) {
        if (validateCredentials()) {
            _registerUiState.value = RegisterUiState.Loading
            Firebase.auth.createUserWithEmailAndPassword(registerForm.email, registerForm.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _registerUiState.value = RegisterUiState.Success
                        Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        _registerUiState.value =
                            RegisterUiState.Error(task.exception?.message ?: "Registration failed")
                        Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
        }
    }
}