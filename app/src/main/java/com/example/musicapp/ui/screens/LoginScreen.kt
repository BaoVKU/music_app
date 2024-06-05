package com.example.musicapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicapp.R
import com.example.musicapp.ui.navigation.HomeDestination
import com.example.musicapp.ui.navigation.LoginDestination
import com.example.musicapp.ui.navigation.NavigationDestination
import com.example.musicapp.ui.navigation.RegisterDestination
import com.example.musicapp.ui.viewmodels.LoginUiState
import com.example.musicapp.ui.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    val loginUiState by loginViewModel.loginUiState.collectAsState()

    if (loginUiState == LoginUiState.Success) {
        onNavigate(HomeDestination)
    }



    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
            Image(
                painter = painterResource(id = R.drawable.squirrel),
                contentDescription = "logo",
                contentScale = ContentScale.Inside
            )
        }
        Text(
            text = LoginDestination.title,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = loginViewModel.loginForm.email,
            onValueChange = {
                loginViewModel.setEmail(it)
            },
            label = { Text(text = "Gmail") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = loginViewModel.loginForm.password,
            onValueChange = {
                loginViewModel.setPassword(it)
            },
            label = { Text(text = "Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        Button(
            onClick = {
                loginViewModel.login(context)
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
        Row {
            Text(text = "Don't have an account?", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Sign up",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable {
                        onNavigate(RegisterDestination)
                    }
            )
        }
    }

}
