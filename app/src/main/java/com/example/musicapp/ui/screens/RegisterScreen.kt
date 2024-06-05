package com.example.musicapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicapp.R
import com.example.musicapp.ui.navigation.HomeDestination
import com.example.musicapp.ui.navigation.LoginDestination
import com.example.musicapp.ui.navigation.NavigationDestination
import com.example.musicapp.ui.navigation.RegisterDestination
import com.example.musicapp.ui.viewmodels.RegisterUiState
import com.example.musicapp.ui.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val context = LocalContext.current
    val registerUiState by registerViewModel.registerUiState.collectAsState()

    if (registerUiState == RegisterUiState.Success) {
        onNavigate(HomeDestination)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
            .imePadding()
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
            Image(
                painter = painterResource(id = R.drawable.squirrel),
                contentDescription = "logo",
                contentScale = ContentScale.Inside
            )
        }
        Text(
            text = RegisterDestination.title,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = registerViewModel.registerForm.email,
            onValueChange = {
                registerViewModel.setEmail(it)
            },
            label = { Text(text = "Gmail") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = registerViewModel.registerForm.password,
            onValueChange = {
                registerViewModel.setPassword(it)
            },
            label = { Text(text = "Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = registerViewModel.registerForm.confirmPassword,
            onValueChange = {
                registerViewModel.setConfirmPassword(it)
            },
            label = { Text(text = "Confirm password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        Button(
            onClick = {
                registerViewModel.register(context = context)
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "Register",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(4.dp)
            )
        }
        Row {
            Text(text = "Already have account?", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Sign in",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable {
                        onNavigate(LoginDestination)
                    }
            )
        }
    }
}
